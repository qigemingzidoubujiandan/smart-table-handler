package com.ruoyi.project.parse.extractor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parse.extractor.unit.UnitConvert;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.PDFTable;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.util.TableUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.project.parse.extractor.unit.UnitExtractConverter.handleAmountUnit;
import static com.ruoyi.project.parse.extractor.unit.UnitExtractor.amountUnitExtract;


/**
 * @author chenl
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Data
public abstract class AbstractTableExtractor<T extends ExtractedResult> implements IExtractor<List<Table>, T> {

    protected final ExtractorConfig config;
    protected T parsedResult;

    protected AbstractTableExtractor(ExtractorConfig config) {
        this.config = config;
        this.parsedResult = createParsedResult();
    }

    protected abstract T createParsedResult();

    @Override
    public T extract(List<Table> tables) {
        if (config.isMergeRow()) {
            mergePDFRow(tables);
        }
        if (config.isMergeSameTitle()) {
            mergeTableByThEqual(tables);
        }
        if (config.isRemoveEmptyRow()) {
            delEmptyRow(tables);
        }
        doExtract(tables);
        return parsedResult;
    }

    protected abstract void doExtract(List<Table> tables);

    protected T matchedData() {
        return parsedResult;
    }

    public abstract boolean parsed();

    public abstract void fillMatchedData(T t);

    /**
     * kv表格单位转换
     *
     * @param map    map
     * @param row    row kv表格的行
     * @param rowKey kv表格的key
     */
    protected static void unitConvert(Map<String, String> map, List<Cell> row, String rowKey) {
        String numbStr = TableUtil.format(row.get(1).text());
        String unitStr = amountUnitExtract(rowKey);
        UnitConvert.Unit byUnit = UnitConvert.Unit.getByUnit(unitStr);
        if (Objects.isNull(byUnit)) {
            map.put(rowKey, handleAmountUnit(numbStr));
        } else {
            String numbConvert = UnitConvert.getAmountConvertFactory().get(byUnit).apply(numbStr);
            map.put(rowKey, numbConvert);
        }
    }

    /**
     * 仿照kv表格单位转换
     * 将转换出的结果返回 自行处理
     */
    private static String getUnitConvert(String cell, String rowKey) {
        String numbStr = TableUtil.format(cell);
        String unitStr = amountUnitExtract(rowKey);
        UnitConvert.Unit byUnit = UnitConvert.Unit.getByUnit(unitStr);
        if (Objects.isNull(byUnit)) {
            return handleAmountUnit(numbStr);
        } else {
            return UnitConvert.getAmountConvertFactory().get(byUnit).apply(numbStr);
        }
    }


    protected static void fillThFunction(List<Function<String, String>> thFunc, List<? extends Cell> thList) {
        // 处理表头单位
        if (CollUtil.isEmpty(thFunc)) {
            thList.stream().map(thCell ->
                            UnitConvert.Unit.getByUnit(String.valueOf(thCell.ext())))
                    .map(unit -> UnitConvert.getAmountConvertFactory().get(unit))
                    .forEach(thFunc::add);
        }
    }

    /**
     * 智能的处理表格样式
     */
    protected static void smartHandleTable(List<Table> tableList) {
        delEmptyRow(tableList);
        // 合并相同标题表格
        mergeTableByThEqual(tableList);
        // 合并因pdf切割，导致cell被切开成的数据
        mergePDFRow(tableList);
        // 从单个格式混乱表格中拆分kv类型表格
        splitKVTable(tableList);
    }

    /**
     * 合并th相同的表格
     */
    protected static void mergeTableByThEqual(List<? extends Table> tableList) {
        Table preTable = null;
        for (int i = tableList.size() - 1; i >= 0; i--) {
            Table table = tableList.get(i);
            List<? extends Cell> th = table.getTh();
            if (preTable == null) {
                preTable = table;
                continue;
            }
            if (thEquals(preTable, table)) {
                List<List<Cell>> data = preTable.getData();
                List<List<Cell>> lists = table.getData().subList(1, table.getData().size());
                data.addAll(lists);
                preTable.setData(data);
                // 删掉当前表格
                tableList.remove(table);
            } else {
                preTable = table;
            }
        }
    }

    private static boolean thEquals(Table table1, Table table2) {
        if ((Objects.isNull(table1) && Objects.nonNull(table2)) || (Objects.nonNull(table1) && Objects.isNull(
                table2))) {
            return false;
        }
        List<? extends Cell> table1Th = table1.getTh();
        List<? extends Cell> table2Th = table2.getTh();
        if (CollUtil.isEmpty(table2Th) || CollUtil.isEmpty(table1Th)) {
            return false;
        }
        if (table1Th.size() != table2Th.size()) {
            return false;
        }

        for (int i = 0; i < table1Th.size(); i++) {
            if (!table1Th.get(i).text().equals(table2Th.get(i).text())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拆分顶部为kv类型表格（因尾部变成list类型表格，从而多出许多空列）
     * 例如：
     * -------------------------------------------------------------------
     * |产品管理人                               | 兴银理财有限责任公司         |
     * -------------------------------------------------------------------
     * |产品托管人                               |兴业银行股份有限公司          |
     * -------------------------------------------------------------------
     * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
     * -------------------------------------------------------------------
     * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
     * -------------------------------------------------------------------
     * <p>
     * 拆分后：两个：
     * <p>
     * -------------------------------------------------------------------
     * |产品管理人                               | 兴银理财有限责任公司         |
     * -------------------------------------------------------------------
     * |产品托管人                               |兴业银行股份有限公司          |
     * -------------------------------------------------------------------
     * 和
     * -------------------------------------------------------------------
     * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
     * -------------------------------------------------------------------
     * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
     * -------------------------------------------------------------------
     */
    private static void splitKVTable(List<Table> tableList) {

        for (int i = tableList.size() - 1; i >= 0; i--) {
            Table table = tableList.get(i);

            // 验证表格是否满足:KV表格 -》 因尾部变成list类型表格，从而多出许多空列
            if (verifyInvalidTableFormat(table)) {
                continue;
            }

            List<List<Cell>> tableData = table.getData();

            // 首部cell为空的索引
            List<Cell> firstCellList = tableData.get(0);
            List<Integer> firstCellEmptyIndexList = obtainEmptyIndex(firstCellList);

            // 尾部cell为空的索引
            List<Cell> lastCellList = tableData.get(tableData.size() - 1);
            List<Integer> lastCellListEmptyIndexList = obtainEmptyIndex(lastCellList);

            // 重新构造表格
            List<List<Cell>> newCellList1 = reshapeTable(tableData, firstCellEmptyIndexList);
            List<List<Cell>> newCellList2 = reshapeTable(tableData, lastCellListEmptyIndexList);

            // 移除当前，添加split后的
            tableList.remove(i);
            Table newTable = new PDFTable();
            newTable.setData(newCellList1);
            tableList.add(newTable);

            Table newTable2 = new PDFTable();
            newTable2.setData(newCellList2);
            tableList.add(newTable2);
        }
    }

    /**
     * 重塑表格
     */
    private static List<List<Cell>> reshapeTable(List<List<Cell>> tableData, List<Integer> firstCellEmptyIndexList) {
        List<List<Cell>> newCellList = Lists.newArrayList();
        outer:
        for (int i = 0; i < tableData.size(); i++) {
            List<Cell> cells = tableData.get(i);
            for (Integer inx : firstCellEmptyIndexList) {
                Cell cell = cells.get(inx);
                if (StrUtil.isNotEmpty(cell.text())) {
                    // 该行记录不符合 跳出，
                    continue outer;
                }
            }

            // 同这组一样的格式
            List<Cell> list = cells.stream().filter(cell -> StrUtil.isNotEmpty(cell.text()))
                    .collect(Collectors.toList());
            newCellList.add(list);
        }
        return newCellList;
    }

    /**
     * 验证无效表格格式
     */
    private static boolean verifyInvalidTableFormat(Table table) {

        // 验证第一列是否有效cell只有两个
        List<? extends Cell> tableTh = table.getTh();
        if (CollUtil.isEmpty(tableTh)) {
            return true;
        }
        long count = tableTh.stream().filter(l -> StrUtil.isNotEmpty(l.text())).count();
        if (count != 2) {
            return true;
        }

        // 验证有效的列上下无效不一致。kv=2列，尾部list最少3列。
        List<Long> collect = table.getData().stream()
                .map(l ->
                        l.stream().filter(r -> StrUtil.isNotEmpty(r.text())).count())
                .distinct()
                .collect(Collectors.toList());

        // 只支持两种格式的不一样
        if (CollUtil.isEmpty(collect) || collect.size() != 2) {
            return true;
        }
        return false;
    }

    /**
     * 获取空字段的索引
     */
    private static List<Integer> obtainEmptyIndex(List<Cell> firstCellList) {
        List<Integer> emptyIndexList = Lists.newArrayList();
        for (int i = 0; i < firstCellList.size(); i++) {
            Cell cell = firstCellList.get(i);
            if (StrUtil.isEmpty(cell.text())) {
                emptyIndexList.add(i);
            }
        }
        return emptyIndexList;
    }

    public void mergePDFRow(String isMergeSameTitle, List<Table> tableList) {
        if ("1".equals(isMergeSameTitle)) {
            mergePDFRow(tableList);
        }
    }

    /**
     * 合并因分页切割的pdf类型表格
     * <p>
     * case:
     * -------------------------------------------------------------------
     * |产品管理人                               | 兴银理财有限责任公司         |
     * -------------------------------------------------------------------
     * |产品托管人                               |兴业银行股份                |
     * -------------------------------------------------------------------
     * <p>
     * -------------------------------------------------------------------
     * |                                       |有限公司                   |
     * -------------------------------------------------------------------
     * |资产净值                                 | 1,534,847,157.79        |
     * -------------------------------------------------------------------
     * <p>
     * 合并完后：
     * -------------------------------------------------------------------
     * |产品管理人                               | 兴银理财有限责任公司         |
     * -------------------------------------------------------------------
     * |产品托管人                               |兴业银行股份有限公司          |
     * -------------------------------------------------------------------
     * |资产净值                                 | 1,534,847,157.79        |
     * -------------------------------------------------------------------
     */
    public static void mergePDFRow(List<? extends Table> tableList) {
        int preIndex = 0;
        for (int curIndex = preIndex + 1; curIndex < tableList.size(); curIndex++) {
            Table pre = tableList.get(preIndex);
            // 之前table最后一个元素 、 只有pdf会有这种切割问题
            if (CollUtil.isEmpty(pre.getData()) || !(pre instanceof PDFTable)) {
                preIndex++;
                continue;
            }
            List<Cell> cellList = pre.getData().get(pre.getData().size() - 1);

            Table next = tableList.get(curIndex);
            // 下个table第一个元素
            List<? extends Cell> nextCellList = next.getTh();
            if (Objects.isNull(nextCellList) || cellList.size() != nextCellList.size()) {
                preIndex++;
                continue;
            }
            boolean hasEmpty = nextCellList.stream().anyMatch(cell -> StrUtil.isEmpty(cell.text()));
            if (hasEmpty) {
                for (int i = 0; i < cellList.size(); i++) {
                    Cell cell1 = cellList.get(i);
                    Cell cell2 = nextCellList.get(i);
                    cell1.setText(cell1.text() + cell2.text());
                }
                List<List<Cell>> data = pre.getData();
                // 移除之前的
                data.remove(data.size() - 1);
                List<List<Cell>> data2 = next.getData();
                data2.remove(0);
                next.setData(data2);
                // 添加合并的
                data.add(cellList);
                pre.setData(data);
            }
            preIndex++;
        }
    }

    public static void delEmptyRow(List<? extends Table> tables) {
        if (tables == null || tables.isEmpty()) {
            return;
        }

        tables.forEach(table -> {
            table.setData(
                    table.getData().stream()
                            .filter(row -> row.stream().anyMatch(cell -> !StringUtils.isEmpty(cell.text())))
                            .collect(Collectors.toList())
            );
        });
    }

}
