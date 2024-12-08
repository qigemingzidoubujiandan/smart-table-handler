package com.ruoyi.project.system.parse.parse.parser.manage;

import com.ruoyi.project.system.parse.parse.extractor.AbstractTextExtractor;
import com.ruoyi.project.system.parse.parse.parser.AbstractTextParser;
import com.ruoyi.project.system.parse.parse.util.AssertUtil;
import lombok.Getter;

public class TextParser<T> {

    @SuppressWarnings("rawtypes")
    private final AbstractTextParser parser;

    private final AbstractTextExtractor<T> extractor;

    @Getter
    private String text;

    private TextParser(AbstractTextParser<?> parser, AbstractTextExtractor<T> extractor) {
        this.parser = parser;
        this.extractor = extractor;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public TextParser<T> parse(Object dataSource) {
        AssertUtil.notNull(dataSource, "数据源为空");
        AssertUtil.notNull(parser, "解析器为空");
        text = parser.parse(dataSource);
        return this;
    }

    public T extract() {
        AssertUtil.notNull(parser, "抽取器为空");
        return extractor.extract(text);
    }

    public static class Builder<T> {

        private AbstractTextParser<?> parser;

        private AbstractTextExtractor<T> extractor;

        private Builder() {

        }

        public Builder<T> parser(AbstractTextParser<?> parser) {
            this.parser = parser;
            return this;
        }

        public Builder<T> extractor(AbstractTextExtractor<T> extractor) {
            this.extractor = extractor;
            return this;
        }

        public TextParser<T> build() {
            return new TextParser<>(parser, extractor);
        }
    }
}
