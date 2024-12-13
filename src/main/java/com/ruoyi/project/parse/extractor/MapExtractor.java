package com.ruoyi.project.parse.extractor;


import cn.hutool.core.collection.CollUtil;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.util.TableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class MapExtractor extends AbstractTableExtractor<Map<String, String>> {
    protected List<? extends Table> unresolvedTables;

    public MapExtractor(String[] conditions, int expectParseRowSize) {
        this.setParsedResult(new HashMap<>());
        this.setConditions(conditions);
        this.setExpectParseRowSize(expectParseRowSize);
    }

    public MapExtractor(String[] conditions) {
        this.setParsedResult(new HashMap<>());
        this.setConditions(conditions);
        this.setExpectParseRowSize(-1);
    }

    @Override
    public boolean parsed() {
        // 解析结果已经大于需要的结果了，暂时算解析完毕，不然还要继续向下增加
        return getParsedResult().size() >= getExpectParseRowSize();
    }

    @Override
    public void fillMatchedData(Map<String, String> map) {
        getParsedResult().putAll(map);
    }


    @Override
    public Map<String, String> extract(List<Table> tables) {
        doExtract(tables);
        return getParsedResult();
    }

    void doExtract(List<? extends Table> tables) {
        this.unresolvedTables = tables;
        extract_KV();
    }


    /**
     * 解析 k-v 形式 (也就是只包含两个表格的形式)
     */
    protected void extract_KV() {
        for (Table table : unresolvedTables) {
            extract_KV(table.getData());
        }
    }

    /**
     * 解析 k-v 形式 (也就是只包含两个表格的形式)
     */
    protected void extract_KV(List<List<Cell>> rows) {
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
        String[] conditions = getConditions();
        for (String condition : conditions) {
            String rowKey = TableUtil.format(cell.text());
            if (rowKey.contains(condition)) {
                tempMap.clear();
                try {
                    unitConvert(tempMap, row, rowKey);
                } catch (Exception e) {
                    log.error("解析异常", e);
                }
                this.fillMatchedData(tempMap);
            }
        }
    }

}