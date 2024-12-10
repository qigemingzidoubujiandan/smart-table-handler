//package com.ruoyi.project.tool.parse.parser.manage;
//
//import cn.hutool.core.collection.CollUtil;
//import com.google.common.collect.Lists;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class TableParser<T extends TableExtractor> {
//
//    @SuppressWarnings("rawtypes")
//    private final List<AbstractTableParser> parserList;
//
//    private final T tableExtractor;
//
//    @Getter
//    private List<Table> tableList;
//
//    @SuppressWarnings("rawtypes")
//    private TableParser(List<AbstractTableParser> parserList, T tableExtractor) {
//        this.parserList = parserList;
//        this.tableExtractor = tableExtractor;
//    }
//
//    public static <T extends TableExtractor> Builder<T> builder() {
//        return new Builder<>();
//    }
//
//    @SuppressWarnings("unchecked")
//    public TableParser<T> parse(Object dataSource) {
//        AssertUtil.notNull(dataSource, "数据源为空");
//        AssertUtil.notEmpty(parserList, "解析器为空");
//        tableList = parserList.stream().filter(Objects::nonNull).map(p -> (List<Table>) p.parse(dataSource))
//                .filter(CollUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
//        return this;
//    }
//
//    public Result<T> extract() {
//        AssertUtil.notNull(tableExtractor, "抽取器为空");
//        // 抽取表格
//        tableExtractor.extract(tableList);
//        return new Result<>(tableExtractor);
//    }
//
//    public static class Builder<T extends TableExtractor> {
//
//        @SuppressWarnings("rawtypes")
//        private final List<AbstractTableParser> parserList = Lists.newArrayList();
//
//        private T tableExtractor;
//
//        private Builder() {
//
//        }
//
//        @SuppressWarnings("rawtypes")
//        public Builder<T> parser(AbstractTableParser parser) {
//            parserList.add(parser);
//            return this;
//        }
//
//        public Builder<T> extractor(T tableExtractor) {
//            this.tableExtractor = tableExtractor;
//            return this;
//        }
//
//        public TableParser<T> build() {
//            return new TableParser<>(parserList, tableExtractor);
//        }
//    }
//
//    /**
//     * 解析结果
//     */
//    @AllArgsConstructor
//    public static class Result<T extends TableExtractor> {
//
//        private Result() {
//
//        }
//
//        /**
//         * 表格抽取结果
//         */
//        @Getter
//        private T tableExtractRet;
//    }
//}
