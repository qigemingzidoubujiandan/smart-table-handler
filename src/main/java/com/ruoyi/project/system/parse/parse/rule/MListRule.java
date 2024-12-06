package com.ruoyi.project.system.parse.parse.rule;

import java.util.*;

public class MListRule extends AbstractTableParseRule<Map<String, List<String>>> {

    public MListRule(String[] conditions) {
        this.setParsedResult(new HashMap<>());
        this.setConditions(conditions);
        this.setExpectParseRowSize(-1);
    }

    @Override
    public boolean parsed() {
        return false;
    }

    @Override
    public void fillMatchedData(Map<String, List<String>> stringListMap) {
        Map<String, List<String>> parsedResult = getParsedResult();
        Set<String> keys = stringListMap.keySet();
        keys.forEach(key -> {
            List<String> list = new ArrayList<>();
            if (parsedResult.containsKey(key)) {
                list = parsedResult.get(key);
            }
            list.addAll(stringListMap.get(key));
            parsedResult.put(key, list);
        });
    }
}
