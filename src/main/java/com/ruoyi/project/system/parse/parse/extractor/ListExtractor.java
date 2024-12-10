package com.ruoyi.project.system.parse.parse.extractor;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.parse.parse.domain.Cell;
import com.ruoyi.project.system.parse.parse.domain.Table;
import com.ruoyi.project.system.parse.parse.domain.FileTypeEnum;
import com.ruoyi.project.system.parse.parse.util.CollectionUtil;
import com.ruoyi.project.system.parse.parse.util.TableUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.project.system.parse.parse.convert.UnitExtractConverter.handleAmountUnit;

/**
 * list类的规则
 * -------------------------------------------------------------------
 * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
 * -------------------------------------------------------------------
 * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 * |兴银稳添利短债 6 号  |  9K810056         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 */
@Slf4j
@Data
public class ListExtractor extends AbstractTableExtractor<List<List<String>>> {

    protected List<? extends Table> unresolvedTables;
    /**
     * 中断表格条件，满足条件就停止匹配
     */
    protected String interpretConditions;

    /**
     * 合并相同标题表格（0不处理 1处理）
     */
    protected String isMergeSameTitle;

    /**
     * 合并表格行
     */
    protected String isMergeRow;

    public ListExtractor(String[] conditions, int expectParseRowSize, String interpretConditions, String isMergeRow, String isMergeSameTitle) {
        this.setParsedResult(new ArrayList<>(8));
        this.setConditions(conditions);
        this.setExpectParseRowSize(expectParseRowSize);
        this.setInterpretConditions(interpretConditions);
        this.setIsMergeSameTitle(isMergeSameTitle);
        this.setIsMergeRow(isMergeRow);
    }


    @Override
    public List<List<String>> extract(List<Table> tables) {
        doExtract(tables);
        return getParsedResult();
    }

    void doExtract(List<? extends Table> unresolvedTables) {
        this.unresolvedTables = unresolvedTables;
        if ("1".equals(isMergeRow)) {
            mergePDFRow(unresolvedTables);
        }
        if ("1".equals(isMergeSameTitle)) {
            mergeTableByThEqual(unresolvedTables);
        }
        fuzzyMatchingExtractTable();
    }


    @Override
    public boolean parsed() {
        return getParsedResult().size() >= getExpectParseRowSize() && getExpectParseRowSize() > 0;
    }

    @Override
    public void fillMatchedData(List<List<String>> list) {
        getParsedResult().addAll(list);
    }

    /**
     * 模糊匹配抽取整个表格的内容信息
     * 表头模糊匹配
     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
     *
     * @param table 表格信息
     */
    protected int fuzzyMatchingExtractTableCell(Table table, List<Function<String, String>> thFunc,
                                                boolean isVerifyTableTitle) {
        return extractTableCell(table, thFunc, isVerifyTableTitle, (v1, v2) -> {
                    if (StringUtils.isEmpty(v1.toString()) && StringUtils.isEmpty(v2.toString())) {
                        return false;
                    }
                    return StringUtils.isEmpty(v1.toString()) || StringUtils.isEmpty(v2.toString())
                            || !(TableUtil.removeBrackets(v1.toString()).contains(TableUtil.removeBrackets(v2.toString()))
                            || TableUtil.removeBrackets(v2.toString()).contains(TableUtil.removeBrackets(v1.toString())));
                }
        );
    }

    /**
     * 模糊匹配抽取完整的表格的内容信息
     * 表头模糊匹配
     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
     */
    protected void fuzzyMatchingExtractTable() {
        try {
            extractTable(true);
        } catch (Exception e) {
            log.error("extractTable error : msg:{}", e.getMessage(), e);
        }
    }

    /**
     * 模糊匹配抽取整个表格的内容信息。
     * 解决表格存在不同表头相同含义的场景。
     * ****确保避免表头交叉定位到其他表格
     */
    protected void fuzzyMatchingAnyThExtractTable(List<Table> pdfTables) {
        try {
            extractAnyThTable();
        } catch (Exception e) {
            log.error("extractTable error : msg:{}", e.getMessage(), e);
        }
    }

    /**
     * 抽取整个表格的内容信息
     * 表头全匹配
     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
     *
     * @param table 表格信息
     */
    protected int exactMatchingExtractTableCell(Table table,
                                                List<Function<String, String>> thFunc, boolean isVerifyTableTitle) {
        return extractTableCell(table, thFunc, isVerifyTableTitle, (v1, v2) ->
                StringUtils.isEmpty(v1.toString()) || StringUtils.isEmpty(v2.toString())
                        || !(Objects.equals(TableUtil.removeBrackets(v1.toString()),
                        TableUtil.removeBrackets(v2.toString()))));
    }

    /**
     * 模糊匹配抽取整个表格的内容信息。
     * <p>
     * 解决表格存在不同表头相同含义的场景。
     * ex：资产top10表头
     * ① 序号、资产代码、资产名称、金额、占产品比例
     * ② 序号、产品代码、资产简称、摊余成本、占基金比例
     * ③ 序号、资产代码、资产名称、摊余成本、占基金比例
     * </p>
     * 表头模糊匹配 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
     */
    protected int fuzzyMatchingAnyThExtractTableCell(Table table,
                                                     List<Function<String, String>> thFunc, boolean isVerifyTableTitle) {
        return extractTableCell(table, thFunc, isVerifyTableTitle,
                (v1, v2) -> {
                    String value1 = TableUtil.removeBrackets(v1.toString());
                    String value2 = TableUtil.removeBrackets(v2.toString());
                    if (StrUtil.isBlank(value1) || StrUtil.isBlank(value2)) {
                        return true;
                    }
                    return Arrays.stream(value2.split("\\|")).noneMatch(v -> value1.contains(v) || v.contains(value1));
                });
    }

    /**
     * 抽取完整的表格的内容信息
     * 表头全匹配
     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
     */
    protected void exactMatchingExtractTable() {
        try {
            extractTable(false);
        } catch (Exception e) {
            log.error("extractTable error : msg:{}", e.getMessage(), e);
        }
    }

    /**
     * 抽取整个表格的内容信息
     * 注意    !!!!!!!!!!!   如果表格是pdf解析出来的,被pdf分页所割裂开，此处取得数据不全
     *
     * @param table 表格信息
     */
    private int extractTableCell(Table table, List<Function<String, String>> thFunc,
                                 boolean isVerifyTableTitle,
                                 BiPredicate<Object, Object> thMatchPredicate) {
        if (this.parsed()) {
            return ExtractRet.FINISH;
        }
        List<List<Cell>> rows = table.getData();
        if (CollUtil.isEmpty(rows)) {
            return ExtractRet.FAIL;
        }

        // 校验表头是否匹配
        List<? extends Cell> thList = table.getTh();
        // 去掉换行
        Object[] texts = thList.stream().map(Cell::text).map(format()).toArray();
        // 验证表格列数
        if (texts.length != this.getConditions().length) {
            return ExtractRet.FAIL;
        }
        // 验证表头 -> 多页的表格第一次才验证
        if (isVerifyTableTitle) {
            Object[] titles = Arrays.stream(this.getConditions()).map(String::trim).toArray();
            for (int i = 0; i < texts.length; i++) {
                if (thMatchPredicate.test(texts[i], titles[i])) {
                    return ExtractRet.FAIL;
                }
            }
        }
        // 填充处理表头函数
        fillThFunction(thFunc, thList);
        // 是否强制结束匹配 可能有的表格行数不固定，但是被切割后又无法确定下一个是不是上一个表格得延续，需要自定义处理
        if (forceFinish(rows, isVerifyTableTitle)) {
            return ExtractRet.FINISH;
        }
        List<List<String>> list = new ArrayList<>(10);
        for (int i = 0; i < thFunc.size(); i++) {
            Function<String, String> function = thFunc.get(i);
            if (Objects.isNull(function)) {
                continue;
            }
            for (List<Cell> dataCells : rows) {
                Cell cell = CollectionUtil.safeGet(dataCells, i);
                if (Objects.nonNull(cell)) {
                    cell.setText(handleAmountUnit(function.apply(TableUtil.format(cell.text()))));
                }
            }
        }
        rows.forEach(r -> list.add(r.stream().map(Cell::text).map(format()).collect(Collectors.toList())));
        // 回调填充数据
        this.fillMatchedData(list);
        // pdf需要处理切割问题
        if (!parsed() && Objects.equals(table.source(), FileTypeEnum.PDF)) {
            return ExtractRet.UNFINISHED;
        }
        return ExtractRet.FINISH;
    }

    /**
     * 强制结束
     */
    protected boolean forceFinish(List<List<Cell>> rows, boolean isVerifyTableTitle) {
        return forceFinish(rows);
    }

    /**
     * 强制结束
     */
    protected boolean forceFinish(List<List<Cell>> rows) {
        Object o = this.matchedData();
        if (CollUtil.isEmpty(rows)) {
            return true;
        }
        if (o instanceof List) {
            List<List<?>> list = (List<List<?>>) o;
            if (list.isEmpty() || list.get(0).isEmpty()) {
                return false;
            }
            if (StrUtil.isNotEmpty(interpretConditions)) {
                for (List<Cell> cells : rows) {
                    for (Cell cell : cells) {
                        String text = cell.text();
                        return text.contains(interpretConditions);
                    }
                }
            }
            // 非空 ,进行匹配验证 两个表格列不一致则结束匹配
            return list.get(0).size() != Optional.of(rows.get(0)).orElse(Collections.emptyList()).size();
        } else {
            // 只对list 表格进行处理
            return false;
        }
    }


    /**
     * 抽取完整的表格的内容信息
     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
     */
    protected void extractTable(boolean fuzzyMatching) {
        outer:
        for (int i = 0; i < unresolvedTables.size(); i++) {
            Table table = unresolvedTables.get(i);
            // 抽取过的就不必再抽取了，这里只考虑数据被一个地方用
            if (table.isExtracted()) {
                continue;
            }
            // 该规则已经解析完毕
            if (parsed()) {
                continue;
            }
            int result;
            // 处理表头单位
            List<Function<String, String>> thFunc = new ArrayList<>(8);
            if (fuzzyMatching) {
                result = fuzzyMatchingExtractTableCell(table, thFunc, true);
            } else {
                result = exactMatchingExtractTableCell(table, thFunc, true);
            }
            if (result == ExtractRet.FINISH) {
                table.setExtracted(true);
                // 如果抽取得结果完成，结束不必要的循环
                break;
            }
            if (result == ExtractRet.UNFINISHED) {
                table.setExtracted(true);
            }
            // 如果表格类型是pdf,被分页切割了，需要继续进行抽取，重下一个、下下一个....表格进行抽取
            while (Objects.equals(table.source(), FileTypeEnum.PDF) && i + 1 < unresolvedTables.size()
                    && result == ExtractRet.UNFINISHED) {
                Table nextPDFTable = unresolvedTables.get(++i);
                if (fuzzyMatching) {
                    result = fuzzyMatchingExtractTableCell(nextPDFTable, thFunc, false);
                } else {
                    result = exactMatchingExtractTableCell(nextPDFTable, thFunc, false);
                }
                // 失败表示该表格未满足期望的条数，但是接下来的表格却因为 列不符合 等原因失败，说明该表格已经没有了后续，直接退出
                if (result == ExtractRet.FAIL) {
                    table.setExtracted(true);
                    break outer;
                }
                if (result == ExtractRet.UNFINISHED) {
                    nextPDFTable.setExtracted(true);
                }
            }
        }

    }

    /**
     * 抽取完整的表格的内容信息
     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
     * 模糊匹配抽取整个表格的内容信息。
     * <p>
     * 解决表格存在不同表头相同含义的场景。
     * ex：资产top10表头
     * ① 序号、资产代码、资产名称、金额、占产品比例
     * ② 序号、产品代码、资产简称、摊余成本、占基金比例
     * ③ 序号、资产代码、资产名称、摊余成本、占基金比例
     * </p>
     */
    protected void extractAnyThTable() {
        outer:
        for (int i = 0; i < unresolvedTables.size(); i++) {
            Table table = unresolvedTables.get(i);
            // 抽取过的就不必再抽取了，这里只考虑数据被一个地方用
            if (table.isExtracted()) {
                continue;
            }
            // 该规则已经解析完毕
            if (parsed()) {
                continue;
            }
            // 处理表头单位
            List<Function<String, String>> thFunc = new ArrayList<>(8);
            int result = fuzzyMatchingAnyThExtractTableCell(table, thFunc, true);
            if (result == ExtractRet.FINISH) {
                table.setExtracted(true);
                // 如果抽取得结果完成，结束不必要的循环
                break;
            }
            if (result == ExtractRet.UNFINISHED) {
                table.setExtracted(true);
            }
            // 如果表格类型是pdf,被分页切割了，需要继续进行抽取，重下一个、下下一个....表格进行抽取
            while (Objects.equals(table.source(), FileTypeEnum.PDF) && i + 1 < unresolvedTables.size()
                    && result == ExtractRet.UNFINISHED) {
                Table nextPDFTable = unresolvedTables.get(++i);
                result = fuzzyMatchingAnyThExtractTableCell(nextPDFTable, thFunc, false);
                // 失败表示该表格未满足期望的条数，但是接下来的表格却因为 列不符合 等原因失败，说明该表格已经没有了后续，直接退出
                if (result == ExtractRet.FAIL) {
                    table.setExtracted(true);
                    break outer;
                }
                if (result == ExtractRet.UNFINISHED) {
                    nextPDFTable.setExtracted(true);
                }
            }
        }
    }


    private static Function<String, String> format() {
        return r -> r.replaceAll("\\s*|\r|\n|\t", "");
    }


    private static class ExtractRet {

        /**
         * 完成
         */
        public static final int FINISH = 1;
        /**
         * 未完
         */
        public static final int UNFINISHED = 2;
        /**
         * 失败
         */
        public static final int FAIL = 3;
    }
}
