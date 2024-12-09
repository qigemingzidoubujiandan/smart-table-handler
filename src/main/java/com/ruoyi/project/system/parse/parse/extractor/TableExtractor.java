//package com.ruoyi.project.system.parse.parse.extractor;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.ArrayUtil;
//import cn.hutool.core.util.StrUtil;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.project.system.parse.parse.convert.UnitConvert;
//import com.ruoyi.project.system.parse.parse.domain.Cell;
//import com.ruoyi.project.system.parse.parse.domain.PDFTable;
//import com.ruoyi.project.system.parse.parse.domain.Table;
//import com.ruoyi.project.system.parse.parse.domain.TableSourceEnum;
//import com.ruoyi.project.system.parse.parse.util.CollectionUtil;
//import com.ruoyi.project.system.parse.parse.util.TableUtil;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.compress.utils.Lists;
//
//import java.util.*;
//import java.util.function.BiPredicate;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static com.ruoyi.project.system.parse.parse.convert.UnitExtractConverter.handleAmountUnit;
//import static com.ruoyi.project.system.parse.parse.convert.UnitExtractor.amountUnitExtract;
//
//
//@SuppressWarnings("rawtypes")
//@Slf4j
//@Data
//public abstract class TableExtractor implements IExtractor<List<Table>, Void> {
//
//    /**
//     * 模糊匹配抽取整个表格的内容信息
//     * 表头模糊匹配
//     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
//     *
//     * @param table      表格信息
//     * @param tableParse 表格匹配条件
//     */
//    protected int fuzzyMatchingExtractTableCell(Table table, TableParseRule tableParse,
//                                                List<Function<String, String>> thFunc, boolean isVerifyTableTitle) {
//        return extractTableCell(table, tableParse, thFunc, isVerifyTableTitle, (v1, v2) -> {
//                    if (StringUtils.isEmpty(v1.toString()) && StringUtils.isEmpty(v2.toString())) {
//                        return false;
//                    }
//                    return StringUtils.isEmpty(v1.toString()) || StringUtils.isEmpty(v2.toString())
//                            || !(TableUtil.removeBrackets(v1.toString()).contains(TableUtil.removeBrackets(v2.toString()))
//                            || TableUtil.removeBrackets(v2.toString()).contains(TableUtil.removeBrackets(v1.toString())));
//                }
//        );
//    }
//
//    /**
//     * 模糊匹配抽取完整的表格的内容信息
//     * 表头模糊匹配
//     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
//     *
//     * @param pdfTables  表格信息
//     * @param tableParse 表格匹配条件
//     */
//    protected void fuzzyMatchingExtractTable(List<Table> pdfTables, TableParseRule tableParse) {
//        try {
//            extractTable(pdfTables, tableParse, true);
//        } catch (Exception e) {
//            log.error("extractTable error : msg:{}", e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 模糊匹配抽取整个表格的内容信息。
//     * 解决表格存在不同表头相同含义的场景。
//     * ****确保避免表头交叉定位到其他表格
//     */
//    protected void fuzzyMatchingAnyThExtractTable(List<Table> pdfTables, TableParseRule tableParse) {
//        try {
//            extractAnyThTable(pdfTables, tableParse);
//        } catch (Exception e) {
//            log.error("extractTable error : msg:{}", e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 抽取整个表格的内容信息
//     * 表头全匹配
//     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
//     *
//     * @param table      表格信息
//     * @param tableParse 表格匹配条件
//     */
//    protected int exactMatchingExtractTableCell(Table table, TableParseRule tableParse,
//                                                List<Function<String, String>> thFunc, boolean isVerifyTableTitle) {
//        return extractTableCell(table, tableParse, thFunc, isVerifyTableTitle, (v1, v2) ->
//                StringUtils.isEmpty(v1.toString()) || StringUtils.isEmpty(v2.toString())
//                        || !(Objects.equals(TableUtil.removeBrackets(v1.toString()),
//                        TableUtil.removeBrackets(v2.toString()))));
//    }
//
//    /**
//     * 模糊匹配抽取整个表格的内容信息。
//     * <p>
//     * 解决表格存在不同表头相同含义的场景。
//     * ex：资产top10表头
//     * ① 序号、资产代码、资产名称、金额、占产品比例
//     * ② 序号、产品代码、资产简称、摊余成本、占基金比例
//     * ③ 序号、资产代码、资产名称、摊余成本、占基金比例
//     * </p>
//     * 表头模糊匹配 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
//     *
//     * @param tableParse condition 每列可传入多个，"|"分割
//     */
//    protected int fuzzyMatchingAnyThExtractTableCell(Table table, TableParseRule tableParse,
//                                                     List<Function<String, String>> thFunc, boolean isVerifyTableTitle) {
//        return extractTableCell(table, tableParse, thFunc, isVerifyTableTitle,
//                (v1, v2) -> {
//                    String value1 = TableUtil.removeBrackets(v1.toString());
//                    String value2 = TableUtil.removeBrackets(v2.toString());
//                    if (StrUtil.isBlank(value1) || StrUtil.isBlank(value2)) {
//                        return true;
//                    }
//                    return Arrays.stream(value2.split("\\|")).noneMatch(v -> value1.contains(v) || v.contains(value1));
//                });
//    }
//
//    /**
//     * 抽取完整的表格的内容信息
//     * 表头全匹配
//     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
//     *
//     * @param pdfTables  表格信息
//     * @param tableParse 表格匹配条件
//     */
//    protected void exactMatchingExtractTable(List<Table> pdfTables, TableParseRule tableParse) {
//        try {
//            extractTable(pdfTables, tableParse, false);
//        } catch (Exception e) {
//            log.error("extractTable error : msg:{}", e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 解析 k-v 形式 (也就是只包含两个表格的形式)
//     */
//    protected void extractPDF_KV(List<Table> pdfTables, TableParseRule tableParse) {
//        for (Table table : pdfTables) {
//            extract_KV(table.getData(), tableParse);
//        }
//    }
//
//    /**
//     * 解析 k-v 形式 (也就是只包含两个表格的形式)
//     */
//    protected void extract_KV(List<List<Cell>> rows, TableParseRule tableParse) {
//        if (CollUtil.isEmpty(rows)) {
//            return;
//        }
//        // 通用抽取策略
//        for (List<Cell> row : rows) {
//            // k - v 只处理长度为两列的表格
//            cellFillMatchedData(tableParse, row);
//        }
//    }
//
//    protected void extractPDF_KList(List<Table> pdfTables, TableParseRule tableParse) {
//        for (Table table : pdfTables) {
//            extract_KList(table.getData(), tableParse);
//        }
//    }
//
//    /**
//     * 解析 k-List形式(根据K 匹配所有数据)
//     */
//    protected void extract_KList(List<List<Cell>> rows, TableParseRule tableParse) {
//        if (CollUtil.isEmpty(rows)) {
//            return;
//        }
//        for (List<Cell> row : rows) {
//            //凡是k能匹配上的数据都会拼接在后面的list中
//            cellFillForList(tableParse, row);
//        }
//    }
//
//    /**
//     * @param tableParse tableParse
//     * @param row        kv表格的一行
//     */
//    private static void cellFillMatchedData(TableParseRule tableParse, List<Cell> row) {
//        HashMap<String, String> tempMap = new HashMap<>();
//        if (row.size() != 2) {
//            return;
//        }
//        Cell cell = row.get(0);
//        String[] conditions = tableParse.condition();
//        for (String condition : conditions) {
//            String rowKey = format(cell.text());
//            if (rowKey.contains(condition)) {
//                tempMap.clear();
//                try {
//                    unitConvert(tempMap, row, rowKey);
//                } catch (Exception e) {
//                    log.error("解析异常", e);
//                }
//                tableParse.fillMatchedData(tempMap);
//            }
//        }
//    }
//
//    private static void cellFillForList(TableParseRule tableParse, List<Cell> row) {
//        Cell cell = row.get(0);
//        String[] conditions = tableParse.condition();
//        Map<String, List<String>> matchedData = new HashMap<>();
//        for (String condition : conditions) {
//            String rowKey = format(cell.text());
//            if (StrUtil.isNotBlank(rowKey) && rowKey.contains(condition)) {
//                List<String> list = row.stream().skip(1)
//                        .map(Cell::text)
//                        .map(c -> StrUtil.isNotBlank(c) ? getUnitConvert(c, rowKey) : null)
//                        .filter(StrUtil::isNotEmpty)
//                        .collect(Collectors.toList());
//                matchedData.put(condition, list);
//            }
//        }
//        if (matchedData.size() > 0) {
//            tableParse.fillMatchedData(matchedData);
//        }
//    }
//
//    /**
//     * kv表格单位转换
//     *
//     * @param map    map
//     * @param row    row kv表格的行
//     * @param rowKey kv表格的key
//     */
//    private static void unitConvert(Map<String, String> map, List<Cell> row, String rowKey) {
//        String numbStr = format(row.get(1).text());
//        String unitStr = amountUnitExtract(rowKey);
//        UnitConvert.Unit byUnit = UnitConvert.Unit.getByUnit(unitStr);
//        if (Objects.isNull(byUnit)) {
//            map.put(rowKey, handleAmountUnit(numbStr));
//        } else {
//            String numbConvert = UnitConvert.getAmountConvertFactory().get(byUnit).apply(numbStr);
//            map.put(rowKey, numbConvert);
//        }
//    }
//
//    /**
//     * 仿照kv表格单位转换
//     * 将转换出的结果返回 自行处理
//     */
//    private static String getUnitConvert(String cell, String rowKey) {
//        String numbStr = TableUtil.format(cell);
//        String unitStr = amountUnitExtract(rowKey);
//        UnitConvert.Unit byUnit = UnitConvert.Unit.getByUnit(unitStr);
//        if (Objects.isNull(byUnit)) {
//            return handleAmountUnit(numbStr);
//        } else {
//            return UnitConvert.getAmountConvertFactory().get(byUnit).apply(numbStr);
//        }
//    }
//
//    /**
//     * 抽取整个表格的内容信息
//     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
//     *
//     * @param table      表格信息
//     * @param tableParse 表格匹配条件
//     */
//    private int extractTableCell(Table table, TableParseRule tableParse, List<Function<String, String>> thFunc,
//                                 boolean isVerifyTableTitle,
//                                 BiPredicate<Object, Object> thMatchPredicate) {
//        if (tableParse.parsed()) {
//            return ExtractRet.FINISH;
//        }
//        List<List<Cell>> rows = table.getData();
//        if (CollUtil.isEmpty(rows)) {
//            return ExtractRet.FAIL;
//        }
//
//        // 校验表头是否匹配
//        List<? extends Cell> thList = table.getTh();
//        // 去掉换行
//        Object[] texts = thList.stream().map(Cell::text).map(format()).toArray();
//        // 验证表格列数
//        if (texts.length != tableParse.condition().length) {
//            return ExtractRet.FAIL;
//        }
//        // 验证表头 -> 多页的表格第一次才验证
//        if (isVerifyTableTitle) {
//            Object[] titles = Arrays.stream(tableParse.condition()).map(String::trim).toArray();
//            for (int i = 0; i < texts.length; i++) {
//                if (thMatchPredicate.test(texts[i], titles[i])) {
//                    return ExtractRet.FAIL;
//                }
//            }
//        }
//        // 填充处理表头函数
//        fillThFunction(thFunc, thList);
//        // 是否强制结束匹配 可能有的表格行数不固定，但是被切割后又无法确定下一个是不是上一个表格得延续，需要自定义处理
//        if (forceFinish(tableParse, rows, isVerifyTableTitle)) {
//            return ExtractRet.FINISH;
//        }
//        List<List<String>> list = new ArrayList<>(10);
//        for (int i = 0; i < thFunc.size(); i++) {
//            Function<String, String> function = thFunc.get(i);
//            if (Objects.isNull(function)) {
//                continue;
//            }
//            for (List<Cell> dataCells : rows) {
//                Cell cell = CollectionUtil.safeGet(dataCells, i);
//                if (Objects.nonNull(cell)) {
//                    cell.setText(handleAmountUnit(function.apply(format(cell.text()))));
//                }
//            }
//        }
//        rows.forEach(r -> list.add(r.stream().map(Cell::text).map(format()).collect(Collectors.toList())));
//        // 回调填充数据
//        tableParse.fillMatchedData(list);
//        // pdf需要处理切割问题
//        if (!tableParse.parsed() && Objects.equals(table.source(), TableSourceEnum.PDF)) {
//            return ExtractRet.UNFINISHED;
//        }
//        return ExtractRet.FINISH;
//    }
//
//    private static void fillThFunction(List<Function<String, String>> thFunc, List<? extends Cell> thList) {
//        // 处理表头单位
//        if (CollUtil.isEmpty(thFunc)) {
//            thList.stream().map(thCell ->
//                            UnitConvert.Unit.getByUnit(String.valueOf(thCell.ext())))
//                    .map(unit -> UnitConvert.getAmountConvertFactory().get(unit))
//                    .forEach(thFunc::add);
//        }
//    }
//
//
//    /**
//     * 抽取完整的表格的内容信息
//     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
//     *
//     * @param tables     表格信息
//     * @param tableParse 表格匹配条件
//     */
//    protected void extractTable(List<Table> tables, TableParseRule tableParse, boolean fuzzyMatching) {
//        outer:
//        for (int i = 0; i < tables.size(); i++) {
//            Table table = tables.get(i);
//            // 抽取过的就不必再抽取了，这里只考虑数据被一个地方用
//            if (table.isExtracted()) {
//                continue;
//            }
//            // 该规则已经解析完毕
//            if (tableParse.parsed()) {
//                continue;
//            }
//            int result;
//            // 处理表头单位
//            List<Function<String, String>> thFunc = new ArrayList<>(8);
//            if (fuzzyMatching) {
//                result = fuzzyMatchingExtractTableCell(table, tableParse, thFunc, true);
//            } else {
//                result = exactMatchingExtractTableCell(table, tableParse, thFunc, true);
//            }
//            if (result == ExtractRet.FINISH) {
//                table.setExtracted(true);
//                // 如果抽取得结果完成，结束不必要的循环
//                break;
//            }
//            if (result == ExtractRet.UNFINISHED) {
//                table.setExtracted(true);
//            }
//            // 如果表格类型是pdf,被分页切割了，需要继续进行抽取，重下一个、下下一个....表格进行抽取
//            while (Objects.equals(table.source(), TableSourceEnum.PDF) && i + 1 < tables.size()
//                    && result == ExtractRet.UNFINISHED) {
//                Table nextPDFTable = tables.get(++i);
//                if (fuzzyMatching) {
//                    result = fuzzyMatchingExtractTableCell(nextPDFTable, tableParse, thFunc, false);
//                } else {
//                    result = exactMatchingExtractTableCell(nextPDFTable, tableParse, thFunc, false);
//                }
//                // 失败表示该表格未满足期望的条数，但是接下来的表格却因为 列不符合 等原因失败，说明该表格已经没有了后续，直接退出
//                if (result == ExtractRet.FAIL) {
//                    table.setExtracted(true);
//                    break outer;
//                }
//                if (result == ExtractRet.UNFINISHED) {
//                    nextPDFTable.setExtracted(true);
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 抽取完整的表格的内容信息
//     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
//     * 模糊匹配抽取整个表格的内容信息。
//     * <p>
//     * 解决表格存在不同表头相同含义的场景。
//     * ex：资产top10表头
//     * ① 序号、资产代码、资产名称、金额、占产品比例
//     * ② 序号、产品代码、资产简称、摊余成本、占基金比例
//     * ③ 序号、资产代码、资产名称、摊余成本、占基金比例
//     * </p>
//     */
//    protected void extractAnyThTable(List<Table> tables, TableParseRule tableParse) {
//        outer:
//        for (int i = 0; i < tables.size(); i++) {
//            Table table = tables.get(i);
//            // 抽取过的就不必再抽取了，这里只考虑数据被一个地方用
//            if (table.isExtracted()) {
//                continue;
//            }
//            // 该规则已经解析完毕
//            if (tableParse.parsed()) {
//                continue;
//            }
//            // 处理表头单位
//            List<Function<String, String>> thFunc = new ArrayList<>(8);
//            int result = fuzzyMatchingAnyThExtractTableCell(table, tableParse, thFunc, true);
//            if (result == ExtractRet.FINISH) {
//                table.setExtracted(true);
//                // 如果抽取得结果完成，结束不必要的循环
//                break;
//            }
//            if (result == ExtractRet.UNFINISHED) {
//                table.setExtracted(true);
//            }
//            // 如果表格类型是pdf,被分页切割了，需要继续进行抽取，重下一个、下下一个....表格进行抽取
//            while (Objects.equals(table.source(), TableSourceEnum.PDF) && i + 1 < tables.size()
//                    && result == ExtractRet.UNFINISHED) {
//                Table nextPDFTable = tables.get(++i);
//                result = fuzzyMatchingAnyThExtractTableCell(nextPDFTable, tableParse, thFunc, false);
//                // 失败表示该表格未满足期望的条数，但是接下来的表格却因为 列不符合 等原因失败，说明该表格已经没有了后续，直接退出
//                if (result == ExtractRet.FAIL) {
//                    table.setExtracted(true);
//                    break outer;
//                }
//                if (result == ExtractRet.UNFINISHED) {
//                    nextPDFTable.setExtracted(true);
//                }
//            }
//        }
//    }
//
//    /**
//     * 强制结束
//     */
//    protected boolean forceFinish(TableParseRule parseRule, List<List<Cell>> rows, boolean isVerifyTableTitle) {
//        return forceFinish(parseRule, rows);
//    }
//
//    /**
//     * 强制结束
//     */
//    protected boolean forceFinish(TableParseRule parseRule, List<List<Cell>> rows) {
//        Object o = parseRule.matchedData();
//        if (CollUtil.isEmpty(rows)) {
//            return true;
//        }
//        if (o instanceof List) {
//            List<List<?>> list = (List<List<?>>) o;
//            if (list.isEmpty() || list.get(0).isEmpty()) {
//                return false;
//            }
//            // 非空 ,进行匹配验证 两个表格列不一致则结束匹配
//            return list.get(0).size() != Optional.of(rows.get(0)).orElse(Collections.emptyList()).size();
//        } else {
//            // 只对list 表格进行处理
//            return false;
//        }
//    }
//
//    /**
//     * 合并th相同的表格
//     */
//    protected void mergeTableByThEqual(List<Table> tableList) {
//        Table preTable = null;
//        for (int i = tableList.size() - 1; i >= 0; i--) {
//            Table table = tableList.get(i);
//            List<? extends Cell> th = table.getTh();
//            if (preTable == null) {
//                preTable = table;
//                continue;
//            }
//            if (thEquals(preTable, table)) {
//                List<List<Cell>> data = preTable.getData();
//                List<List<Cell>> lists = table.getData().subList(1, table.getData().size());
//                data.addAll(lists);
//                preTable.setNotEmptyData(data);
//                // 删掉当前表格
//                tableList.remove(table);
//            } else {
//                preTable = table;
//            }
//        }
//    }
//
//    private boolean thEquals(Table table1, Table table2) {
//        if ((Objects.isNull(table1) && Objects.nonNull(table2)) || (Objects.nonNull(table1) && Objects.isNull(
//                table2))) {
//            return false;
//        }
//        List<? extends Cell> table1Th = table1.getTh();
//        List<? extends Cell> table2Th = table2.getTh();
//        if (CollUtil.isEmpty(table2Th) || CollUtil.isEmpty(table1Th)) {
//            return false;
//        }
//        if (table1Th.size() != table2Th.size()) {
//            return false;
//        }
//
//        for (int i = 0; i < table1Th.size(); i++) {
//            if (!table1Th.get(i).text().equals(table2Th.get(i).text())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 【完全匹配】表头是否包含传入的参数数组
//     */
//    protected static boolean thContainsAll(List<? extends Cell> th, String... textArr) {
//        if (ArrayUtil.isEmpty(textArr)) {
//            return false;
//        }
//        List<String> thTextList = th.stream().map(Cell::text).collect(Collectors.toList());
//        return thTextList.containsAll(Arrays.asList(textArr));
//    }
//
//    /**
//     * 【模糊匹配】表头是否包含传入的参数数组
//     */
//    protected static boolean thFuzzyContainsAll(List<? extends Cell> th, String... textArr) {
//        if (ArrayUtil.isEmpty(textArr)) {
//            return false;
//        }
//        List<String> thTextList = th.stream().map(Cell::text).collect(Collectors.toList());
//        return Arrays.stream(textArr)
//                .allMatch(text -> thTextList.stream().anyMatch(thText -> thText.contains(text)));
//    }
//
//    public static String format(String text) {
//        if (Objects.isNull(text)) {
//            return null;
//        }
//        return text.replaceAll("\\s*|\r|\n|\t", "").trim();
//    }
//
//    private static Function<String, String> format() {
//        return r -> r.replaceAll("\\s*|\r|\n|\t", "");
//    }
//
//    /**
//     * 智能的处理表格样式
//     */
//    protected void smartHandleTable(List<Table> tableList) {
//        // 合并相同标题表格
//        mergeTableByThEqual(tableList);
//        // 合并因pdf切割，导致cell被切开成的数据
//        mergePDFRow(tableList);
//        // 从单个格式混乱表格中拆分kv类型表格
//        splitKVTable(tableList);
//    }
//
//    /**
//     * 拆分顶部为kv类型表格（因尾部变成list类型表格，从而多出许多空列）
//     * 例如：
//     * -------------------------------------------------------------------
//     * |产品管理人                               | 兴银理财有限责任公司         |
//     * -------------------------------------------------------------------
//     * |产品托管人                               |兴业银行股份有限公司          |
//     * -------------------------------------------------------------------
//     * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
//     * -------------------------------------------------------------------
//     * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
//     * -------------------------------------------------------------------
//     * <p>
//     * 拆分后：两个：
//     * <p>
//     * -------------------------------------------------------------------
//     * |产品管理人                               | 兴银理财有限责任公司         |
//     * -------------------------------------------------------------------
//     * |产品托管人                               |兴业银行股份有限公司          |
//     * -------------------------------------------------------------------
//     * 和
//     * -------------------------------------------------------------------
//     * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
//     * -------------------------------------------------------------------
//     * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
//     * -------------------------------------------------------------------
//     */
//    private void splitKVTable(List<Table> tableList) {
//
//        for (int i = tableList.size() - 1; i >= 0; i--) {
//            Table table = tableList.get(i);
//
//            // 验证表格是否满足:KV表格 -》 因尾部变成list类型表格，从而多出许多空列
//            if (verifyInvalidTableFormat(table)) {
//                continue;
//            }
//
//            List<List<Cell>> tableData = table.getData();
//
//            // 首部cell为空的索引
//            List<Cell> firstCellList = tableData.get(0);
//            List<Integer> firstCellEmptyIndexList = obtainEmptyIndex(firstCellList);
//
//            // 尾部cell为空的索引
//            List<Cell> lastCellList = tableData.get(tableData.size() - 1);
//            List<Integer> lastCellListEmptyIndexList = obtainEmptyIndex(lastCellList);
//
//            // 重新构造表格
//            List<List<Cell>> newCellList1 = reshapeTable(tableData, firstCellEmptyIndexList);
//            List<List<Cell>> newCellList2 = reshapeTable(tableData, lastCellListEmptyIndexList);
//
//            // 移除当前，添加split后的
//            tableList.remove(i);
//            Table newTable = new PDFTable();
//            newTable.setNotEmptyData(newCellList1);
//            tableList.add(newTable);
//
//            Table newTable2 = new PDFTable();
//            newTable2.setNotEmptyData(newCellList2);
//            tableList.add(newTable2);
//        }
//    }
//
//    /**
//     * 重塑表格
//     */
//    private static List<List<Cell>> reshapeTable(List<List<Cell>> tableData, List<Integer> firstCellEmptyIndexList) {
//        List<List<Cell>> newCellList = Lists.newArrayList();
//        outer:
//        for (int i = 0; i < tableData.size(); i++) {
//            List<Cell> cells = tableData.get(i);
//            for (Integer inx : firstCellEmptyIndexList) {
//                Cell cell = cells.get(inx);
//                if (StrUtil.isNotEmpty(cell.text())) {
//                    // 该行记录不符合 跳出，
//                    continue outer;
//                }
//            }
//
//            // 同这组一样的格式
//            List<Cell> list = cells.stream().filter(cell -> StrUtil.isNotEmpty(cell.text()))
//                    .collect(Collectors.toList());
//            newCellList.add(list);
//        }
//        return newCellList;
//    }
//
//    /**
//     * 验证无效表格格式
//     */
//    private static boolean verifyInvalidTableFormat(Table table) {
//
//        // 验证第一列是否有效cell只有两个
//        List<? extends Cell> tableTh = table.getTh();
//        if (CollUtil.isEmpty(tableTh)) {
//            return true;
//        }
//        long count = tableTh.stream().filter(l -> StrUtil.isNotEmpty(l.text())).count();
//        if (count != 2) {
//            return true;
//        }
//
//        // 验证有效的列上下无效不一致。kv=2列，尾部list最少3列。
//        List<Long> collect = table.getData().stream()
//                .map(l ->
//                        l.stream().filter(r -> StrUtil.isNotEmpty(r.text())).count())
//                .distinct()
//                .collect(Collectors.toList());
//
//        // 只支持两种格式的不一样
//        if (CollUtil.isEmpty(collect) || collect.size() != 2) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 获取空字段的索引
//     */
//    private static List<Integer> obtainEmptyIndex(List<Cell> firstCellList) {
//        List<Integer> emptyIndexList = Lists.newArrayList();
//        for (int i = 0; i < firstCellList.size(); i++) {
//            Cell cell = firstCellList.get(i);
//            if (StrUtil.isEmpty(cell.text())) {
//                emptyIndexList.add(i);
//            }
//        }
//        return emptyIndexList;
//    }
//
//    /**
//     * 合并因分页切割的pdf类型表格
//     * <p>
//     * case:
//     * -------------------------------------------------------------------
//     * |产品管理人                               | 兴银理财有限责任公司         |
//     * -------------------------------------------------------------------
//     * |产品托管人                               |兴业银行股份                |
//     * -------------------------------------------------------------------
//     * <p>
//     * -------------------------------------------------------------------
//     * |                                       |有限公司                   |
//     * -------------------------------------------------------------------
//     * |资产净值                                 | 1,534,847,157.79        |
//     * -------------------------------------------------------------------
//     * <p>
//     * 合并完后：
//     * -------------------------------------------------------------------
//     * |产品管理人                               | 兴银理财有限责任公司         |
//     * -------------------------------------------------------------------
//     * |产品托管人                               |兴业银行股份有限公司          |
//     * -------------------------------------------------------------------
//     * |资产净值                                 | 1,534,847,157.79        |
//     * -------------------------------------------------------------------
//     */
//    public static void mergePDFRow(List<Table> tableList) {
//        int preIndex = 0;
//        for (int curIndex = preIndex + 1; curIndex < tableList.size(); curIndex++) {
//            Table pre = tableList.get(preIndex);
//            // 之前table最后一个元素 、 只有pdf会有这种切割问题
//            if (CollUtil.isEmpty(pre.getData()) || !(pre instanceof PDFTable)) {
//                preIndex++;
//                continue;
//            }
//            List<Cell> cellList = pre.getData().get(pre.getData().size() - 1);
//
//            Table next = tableList.get(curIndex);
//            // 下个table第一个元素
//            List<? extends Cell> nextCellList = next.getTh();
//            if (Objects.isNull(nextCellList) || cellList.size() != nextCellList.size()) {
//                preIndex++;
//                continue;
//            }
//            boolean hasEmpty = nextCellList.stream().anyMatch(cell -> StrUtil.isEmpty(cell.text()));
//            if (hasEmpty) {
//                for (int i = 0; i < cellList.size(); i++) {
//                    Cell cell1 = cellList.get(i);
//                    Cell cell2 = nextCellList.get(i);
//                    cell1.setText(cell1.text() + cell2.text());
//                }
//                List<List<Cell>> data = pre.getData();
//                // 移除之前的
//                data.remove(data.size() - 1);
//                List<List<Cell>> data2 = next.getData();
//                data2.remove(0);
//                next.setNotEmptyData(data2);
//                // 添加合并的
//                data.add(cellList);
//                pre.setNotEmptyData(data);
//            }
//            preIndex++;
//        }
//    }
//
//
//    private static class ExtractRet {
//
//        /**
//         * 完成
//         */
//        public static final int FINISH = 1;
//        /**
//         * 未完
//         */
//        public static final int UNFINISHED = 2;
//        /**
//         * 失败
//         */
//        public static final int FAIL = 3;
//    }
//}
