//package com.ruoyi.project.system.parse.parse.parser.manage;
//
//import cn.hutool.core.collection.CollUtil;
//import com.google.common.collect.Lists;
//import com.ruoyi.project.system.parse.parse.domain.Table;
//import com.ruoyi.project.system.parse.parse.extractor.TableExtractor;
//import com.ruoyi.project.system.parse.parse.extractor.TextExtractor;
//import com.ruoyi.project.system.parse.parse.parser.AbstractTableParser;
//import com.ruoyi.project.system.parse.parse.parser.IParser;
//import com.ruoyi.project.system.parse.parse.util.AssertUtil;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * 组合解析器。【表格解析器+文本解析器】
// */
//public class CombineParser<T extends TableExtractor, K> {
//
//    @SuppressWarnings("rawtypes")
//    private final List<IParser> parserList;
//
//    private final T tableExtractor;
//
//    private final TextExtractor<K> textExtractor;
//
//    @Getter
//    private List<Table> tableList;
//
//    @Getter
//    private String text;
//
//    private K textExtractRet;
//
//    @SuppressWarnings("rawtypes")
//    private CombineParser(List<IParser> parserList, T tableExtractor, TextExtractor<K> textExtractor) {
//        this.parserList = parserList;
//        this.tableExtractor = tableExtractor;
//        this.textExtractor = textExtractor;
//    }
//
//    public static <T extends TableExtractor, K> Builder<T, K> builder() {
//        return new Builder<>();
//    }
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public CombineParser<T, K> parse(Object dataSource) {
//        AssertUtil.notNull(dataSource, "数据源为空");
//        AssertUtil.notEmpty(parserList, "解析器为空");
//        // 解析器分组处理
//        List<AbstractTableParser> tableParserList = parserList.stream().filter(AbstractTableParser.class::isInstance)
//                .map(p -> (AbstractTableParser<?>) p).collect(Collectors.toList());
//        if (CollUtil.isNotEmpty(tableParserList)) {
//            this.tableList = tableParserList.stream().filter(Objects::nonNull)
//                    .map(p -> (List<Table>) p.parse(dataSource)).filter(CollUtil::isNotEmpty)
//                    .flatMap(Collection::stream).collect(Collectors.toList());
//        }
//
//        parserList.stream().filter(AbstractTextParser.class::isInstance)
//                .map(AbstractTextParser.class::cast).findAny()
//                .ifPresent(textParser -> this.text = textParser.parse(dataSource));
//        return this;
//    }
//
//    public CombineParser<T, K> extract() {
//        AssertUtil.isFalse(null == tableExtractor && null == textExtractor, "抽取器为空");
//        if (null != tableExtractor) {
//            tableExtractor.extract(tableList);
//        }
//        if (null != textExtractor) {
//            this.textExtractRet = textExtractor.extract(text);
//        }
//        return this;
//    }
//
//    public Result<T, K> combine() {
//        return new Result<>(textExtractRet, tableExtractor);
//    }
//
//    public static class Builder<T extends TableExtractor, K> {
//
//        @SuppressWarnings("rawtypes")
//        private final List<IParser> parserList = Lists.newArrayList();
//
//        private T tableExtractor;
//
//        private TextExtractor<K> textExtractor;
//
//        private Builder() {
//
//        }
//
//        public Builder<T, K> parser(IParser<?, ?> parser) {
//            parserList.add(parser);
//            return this;
//        }
//
//        @SuppressWarnings({"unchecked", "rawtypes"})
//        public Builder<T, K> extractor(IExtractor<?, ?> extractor) {
//            if (extractor instanceof TableExtractor) {
//                this.tableExtractor = (T) extractor;
//            } else if (extractor instanceof TextExtractor) {
//                this.textExtractor = (TextExtractor) extractor;
//            }
//            return this;
//        }
//
//        public CombineParser<T, K> build() {
//            return new CombineParser<>(parserList, tableExtractor, textExtractor);
//        }
//    }
//
//    /**
//     * 解析结果
//     */
//    @AllArgsConstructor
//    public static class Result<T extends TableExtractor, K> {
//
//        private Result() {
//
//        }
//
//        /**
//         * 文本抽取结果
//         */
//        @Getter
//        private K textExtractRet;
//
//        /**
//         * 表格抽取结果
//         */
//        @Getter
//        private T tableExtractRet;
//    }
//}
