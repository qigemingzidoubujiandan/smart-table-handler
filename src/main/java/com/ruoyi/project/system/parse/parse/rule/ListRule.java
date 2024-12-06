package com.ruoyi.project.system.parse.parse.rule;


import java.util.ArrayList;
import java.util.List;

/**
 * list类的规则
 *  -------------------------------------------------------------------
 * |下属子份额的销售名称  | 下属子份额的销售代码  | 报告期末下属子份额的产品份额总数|
 * -------------------------------------------------------------------
 * |兴银稳添利短债 5 号  |  9K81005A         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 * |兴银稳添利短债 6 号  |  9K810056         | 1,534,847,157.79          |
 * -------------------------------------------------------------------
 */
public class ListRule extends AbstractTableParseRule<List<List<String>>> {


    public ListRule(String[] conditions, int expectParseRowSize) {
        this.setParsedResult(new ArrayList<>(8));
        this.setConditions(conditions);
        this.setExpectParseRowSize(expectParseRowSize);
    }

    public ListRule(String[] conditions) {
        this.setParsedResult(new ArrayList<>(8));
        this.setConditions(conditions);
        this.setExpectParseRowSize(-1);
    }

    @Override
    public boolean parsed() {
        return getParsedResult().size() >= expectParseRowSize() && expectParseRowSize() > 0;
    }

    @Override
    public void fillMatchedData(List<List<String>> list) {
        getParsedResult().addAll(list);
    }
}
