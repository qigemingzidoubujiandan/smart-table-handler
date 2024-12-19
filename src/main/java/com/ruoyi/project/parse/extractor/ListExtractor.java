package com.ruoyi.project.parse.extractor;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.domain.Enum.FileTypeEnum;
import com.ruoyi.project.parse.domain.Enum.TableMatchMethodEnum;
import com.ruoyi.project.parse.extractor.result.TableExtractedResult;
import com.ruoyi.project.parse.extractor.unit.UnitConvert;
import com.ruoyi.project.parse.util.CollectionUtil;
import com.ruoyi.project.parse.util.TableUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.project.parse.extractor.unit.UnitExtractConverter.handleAmountUnit;

/**
 * list类的规则
 * 单行处理：
 * -------------------------------------------------------------------
 * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
 * -------------------------------------------------------------------
 * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 * |兴银稳添利短债 6 号  |  9K810056         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 * 多行处理：
 * ------------------------------------------------------------------
 * |    销售名称        | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
 * |                  |  ------------------------------------------------
 * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 * |兴银稳添利短债 6 号  |  9K810056         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 */
@Slf4j
@Data
public class ListExtractor extends AbstractTableExtractor<TableExtractedResult> {

    public ListExtractor(ExtractorConfig config) {
        super(config);
    }

    @Override
    protected TableExtractedResult createParsedResult() {
        return new TableExtractedResult(new ArrayList<>(8));
    }

    @Override
    protected void doExtract(List<Table> tables) {
        if (TableMatchMethodEnum.EXACT.equals(config.getTableMatchMethod())) {
            exactMatchingExtractTable(tables);
        } else {
            fuzzyMatchingExtractTable(tables);
        }
    }

    @Override
    public boolean parsed() {
        return getParsedResult().getTableData().size() >= config.getExpectParseRowSize() && config.getExpectParseRowSize() > 0;
    }

    @Override
    public void fillMatchedData(TableExtractedResult result) {
        getParsedResult().getTableData().addAll(result.getTableData());
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
                            || !(Objects.requireNonNull(TableUtil.removeBrackets(v1.toString())).contains(Objects.requireNonNull(TableUtil.removeBrackets(v2.toString())))
                            || Objects.requireNonNull(TableUtil.removeBrackets(v2.toString())).contains(Objects.requireNonNull(TableUtil.removeBrackets(v1.toString()))));
                }
        );
    }

    /**
     * 模糊匹配抽取完整的表格的内容信息
     * 表头模糊匹配
     * 如果是pdf抽取的，可能被分页割开，一直抽取到满足列数为止
     */
    protected void fuzzyMatchingExtractTable(List<Table> tables) {
        try {
            extractTable(tables, true);
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
            extractAnyThTable(pdfTables);
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
    protected void exactMatchingExtractTable(List<Table> tables) {
        try {
            extractTable(tables, false);
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
        if (texts.length != config.getConditions().length) {
            return ExtractRet.FAIL;
        }
        // 验证表头 -> 多页的表格第一次才验证
        if (isVerifyTableTitle) {
            Object[] titles = Arrays.stream(config.getConditions()).map(String::trim).toArray();
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
        this.fillMatchedData(new TableExtractedResult(list));
        // pdf需要处理切割问题
        if (!parsed() && Objects.equals(table.source(), FileTypeEnum.PDF)) {
            return ExtractRet.UNFINISHED;
        }
        return ExtractRet.FINISH;
    }

    /**
     * 强制结束，可以用来覆盖
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
            if (StrUtil.isNotEmpty(config.getInterpretConditions())) {
                for (List<Cell> cells : rows) {
                    for (Cell cell : cells) {
                        String text = cell.text();
                        return text.contains(config.getInterpretConditions());
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
    protected void extractTable(List<Table> tables, boolean fuzzyMatching) {
        outer:
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
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
            while (Objects.equals(table.source(), FileTypeEnum.PDF) && i + 1 < tables.size()
                    && result == ExtractRet.UNFINISHED) {
                Table nextPDFTable = tables.get(++i);
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
    protected void extractAnyThTable(List<Table> tables) {
        outer:
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
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
            while (Objects.equals(table.source(), FileTypeEnum.PDF) && i + 1 < tables.size()
                    && result == ExtractRet.UNFINISHED) {
                Table nextPDFTable = tables.get(++i);
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


    //-------------------------------------------------------------------多行标题处理-------------------------------------

    /**
     *多行标题处理
     *<p>
     * 解决表格存在不同表头相同含义的场景。
     * ex：
     *     产品类型        本期               上期
     *             数量（只） 金额（万元） 数量（只）金额（万元）
     *    匹配条件传：["产品类型","本期", "上期", "数量", "金额", "数量", "金额"];
     *</p>
     * @param table
     * @param thFunc
     * @param isVerifyTableTitle
     * @return
     */
    private int fuzzyMatchingExtractTableByMultipleTitle(Table table, List<Function<String, String>> thFunc,
                                  boolean isVerifyTableTitle) {
        if (this.parsed()) {
            return ExtractRet.FINISH;
        }
        List<List<Cell>> rows = table.getData();
        if (CollUtil.isEmpty(rows)) {
            return ExtractRet.FAIL;
        }

        // 获取多行标题
        List<List<Cell>> multipleTitles = table.getData().subList(0, config.getThMultipleRowNumber());

        // 去掉换行
        Object[] thArr = multipleTitles.stream().flatMap(Collection::stream).map(Cell::text).map(format()).toArray();

        // 非第一次，说明未匹配完，这时候里面已经有了匹配到的数据了，验证表格列数
        if (!isVerifyTableTitle) {
            // 未开启多行标题匹配时，匹配列数
            List<List<String>> tableData = this.getParsedResult().getTableData();
            if (CollUtil.isNotEmpty(tableData)) {
                int size = tableData.get(0).size();
                // 非第一次匹配一下行数是否匹配
                List<? extends Cell> thList = table.getTh();
                if (thList.size() != size) {
                    return ExtractRet.FAIL;
                }
            }
        }

        // 验证表头 -> 多页的表格第一次才验证
        if (isVerifyTableTitle) {
            Object[] titles = Arrays.stream(config.getConditions()).map(String::trim).toArray();
            if (!matchMultipleTitle(thArr, titles)) {
                return ExtractRet.FAIL;
            }
        }
        // 填充处理表头函数
        unitHandle(multipleTitles, thFunc);
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
        this.fillMatchedData(new TableExtractedResult(list));
        // pdf需要处理切割问题
        if (!parsed() && Objects.equals(table.source(), FileTypeEnum.PDF)) {
            return ExtractRet.UNFINISHED;
        }
        return ExtractRet.FINISH;
    }


    /**
     * 对给定的表格数据按列进行处理。
     *
     * @param rows   表格数据，其中每个 List<Cell> 代表一行，Cell 为单元格对象。
     * @param thFunc 每个列对应的处理函数列表。
     */
    public void unitHandle(List<List<Cell>> rows, List<Function<String, String>> thFunc) {
        if (rows == null || rows.isEmpty()) {
            return;
        }

        // 获取列数，假设所有行具有相同的列数
        int columnCount = rows.get(0).size();
        // 确保 thFunc 的大小与列数匹配，使用默认值 null 初始化
        if (thFunc.size() < columnCount) {
            for (int i = thFunc.size(); i < columnCount; i++) {
                thFunc.add(null);
            }
        }

        for (int colIndex = 0; colIndex < columnCount; colIndex++) {
            Function<String, String> unitFunction = null;

            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                List<Cell> row = rows.get(rowIndex);
                if (colIndex >= row.size()) {
                    log.warn("单位处理异常:索引越界:{}", colIndex);
                    break; // 如果某一列的单元格数量不足，停止对该列的处理
                }
                Cell cell = row.get(colIndex);

                // 尝试获取当前单元格的处理函数
                Function<String, String> currentUnitFunction = getUnitFunction(cell);
                if (currentUnitFunction != null) {
                    unitFunction = currentUnitFunction; // 更新处理函数
                }
            }

            // 如果找到了有效的处理函数，则设置到 thFunc 中
            if (unitFunction != null) {
                thFunc.set(colIndex, unitFunction);
            }
        }
    }

    /**
     * 获取处理单位的函数。
     *
     * @param cell 单元格对象
     * @return 如果找到匹配的转换函数则返回该函数；否则返回 null。
     */
    private Function<String, String> getUnitFunction(Cell cell) {
        if (cell == null || cell.ext() == null) {
            return null;
        }

        UnitConvert.Unit unit = UnitConvert.Unit.getByUnit(String.valueOf(cell.ext()));
        return unit != null ? UnitConvert.getAmountConvertFactory().get(unit) : null;
    }

    /**
     * 检查 conditions 中的每一个元素是否都能在 titles 中找到模糊匹配，并且每个条件只匹配一次。
     *
     * @param titles     标题数组
     * @param conditions 条件数组
     * @return 如果 conditions 中的每个元素都在 titles 中存在模糊匹配且只匹配一次，则返回 true；否则返回 false。
     */
    public boolean matchMultipleTitle(Object[] titles, Object[] conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        if (titles == null || titles.length == 0) {
            return false;
        }

        // 将 titles 转换为 List 并过滤掉 null 值，以便后续操作
        List<String> titleList = Arrays.stream(titles)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(StrUtil::isNotEmpty)
                .collect(Collectors.toList());

        // 检查每个 condition 是否存在于 titleList 中，并在匹配后移除
        for (Object condition : conditions) {
            if (condition == null) {
                continue;
            }
            String conditionStr = condition.toString().trim().toLowerCase();
            Iterator<String> iterator = titleList.iterator();
            boolean matched = false;

            while (iterator.hasNext()) {
                String title = iterator.next().trim().toLowerCase();
                if (title.contains(conditionStr)) {
                    iterator.remove(); // 成功匹配后移除该标题
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                log.warn("Condition '{}' not found in titles.", condition);
                return false;
            }
        }

        // 所有条件都成功匹配
        return true;
    }

    //-------------------------------------------------------------------多行标题处理-------------------------------------

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
