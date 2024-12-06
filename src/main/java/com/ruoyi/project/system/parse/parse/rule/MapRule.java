package com.ruoyi.project.system.parse.parse.rule;


import java.util.HashMap;
import java.util.Map;

/**
 * map格式的规则。
 * -------------------------------------------------------------------
 * |产品管理人                               | 兴银理财有限责任公司         |
 * -------------------------------------------------------------------
 * |产品托管人                               |兴业银行股份有限公司          |
 * -------------------------------------------------------------------
 *
 * K:产品管理人  V:兴银理财有限责任公司
 * K:产品托管人  V:兴业银行股份有限公司
 *
 */
public class MapRule extends AbstractTableParseRule<Map<String, String>> {


    public MapRule(String[] conditions, int expectParseRowSize) {
        this.setParsedResult(new HashMap<>());
        this.setConditions(conditions);
        this.setExpectParseRowSize(expectParseRowSize);
    }

    public MapRule(String[] conditions) {
        this.setParsedResult(new HashMap<>());
        this.setConditions(conditions);
        this.setExpectParseRowSize(-1);
    }

    @Override
    public boolean parsed() {
        // 解析结果已经大于需要的结果了，暂时算解析完毕，不然还要继续向下增加
        return getParsedResult().size() >= expectParseRowSize();
    }

    @Override
    public void fillMatchedData(Map<String, String> map) {
        getParsedResult().putAll(map);
    }
}