package com.ruoyi.project.system.parse.parse.parser.manage;

import lombok.Getter;

public class TextParser<T> {

    @SuppressWarnings("rawtypes")
    private final AbstractTextParser parser;

    private final TextExtractor<T> extractor;

    @Getter
    private String text;

    private TextParser(AbstractTextParser<?> parser, TextExtractor<T> extractor) {
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

        private TextExtractor<T> extractor;

        private Builder() {

        }

        public Builder<T> parser(AbstractTextParser<?> parser) {
            this.parser = parser;
            return this;
        }

        public Builder<T> extractor(TextExtractor<T> extractor) {
            this.extractor = extractor;
            return this;
        }

        public TextParser<T> build() {
            return new TextParser<>(parser, extractor);
        }
    }
}
