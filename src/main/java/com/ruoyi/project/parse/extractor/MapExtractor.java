package com.ruoyi.project.parse.extractor;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.Enum.TableMatchMethodEnum;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.unit.UnitConvert;
import com.ruoyi.project.parse.util.TableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ruoyi.project.parse.extractor.unit.UnitExtractConverter.handleAmountUnit;
import static com.ruoyi.project.parse.extractor.unit.UnitExtractor.amountUnitExtract;

/**
 * map格式的规则。
 * -------------------------------------------------------------------
 * |产品管理人                               | 兴银理财有限责任公司         |
 * -------------------------------------------------------------------
 * |产品托管人                               |兴业银行股份有限公司          |
 * -------------------------------------------------------------------
 * <p>
 * K:产品管理人  V:兴银理财有限责任公司
 * K:产品托管人  V:兴业银行股份有限公司
 *
 * @author chenl
 */
@Slf4j
public class MapExtractor extends AbstractTableExtractor<KVExtractedResult> {

    public MapExtractor(ExtractorConfig config) {
        super(config);
    }

    @Override
    protected KVExtractedResult createParsedResult() {
        return new KVExtractedResult(new HashMap<>());
    }

    @Override
    protected void doExtract(List<Table> tables) {
        extract_KV(tables);
    }

    @Override
    public boolean parsed() {
        return getParsedResult().getKeyValuePairs().size() >= config.getExpectParseRowSize();
    }

    @Override
    public void fillMatchedData(KVExtractedResult result) {
        getParsedResult().getKeyValuePairs().putAll(result.getKeyValuePairs());
    }

    /**
     * 解析 k-v 形式 (也就是只包含两个表格的形式)
     */
    protected void extract_KV(List<Table> tables) {
        for (Table table : tables) {
            doExtract_KV(table.getData());
        }
    }

    /**
     * 解析 k-v 形式 (也就是只包含两个表格的形式)
     */
    protected void doExtract_KV(List<List<Cell>> rows) {
        if (CollUtil.isEmpty(rows)) {
            return;
        }
        // 通用抽取策略
        for (List<Cell> row : rows) {
            // k - v 只处理长度为两列的表格
            cellFillMatchedData(row);
        }
    }

    /**
     * @param row kv表格的一行
     */
    private void cellFillMatchedData(List<Cell> row) {
        HashMap<String, String> tempMap = new HashMap<>();
        if (row.size() != 2) {
            return;
        }
        Cell cell = row.get(0);
        String[] conditions = config.getConditions();
        for (String condition : conditions) {
            String rowKey = TableUtil.format(cell.text());
            boolean match;
            if (TableMatchMethodEnum.EXACT.equals(config.getTableMatchMethod())) {
                match = CharSequenceUtil.equals(rowKey, condition);
            } else {
                match = CharSequenceUtil.contains(rowKey, condition);
            }
            if (match) {
                tempMap.clear();
                try {
                    unitConvert(tempMap, row, rowKey);
                } catch (Exception e) {
                    log.error("解析异常", e);
                }
                this.fillMatchedData(new KVExtractedResult(tempMap));
            }
        }
    }


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

}