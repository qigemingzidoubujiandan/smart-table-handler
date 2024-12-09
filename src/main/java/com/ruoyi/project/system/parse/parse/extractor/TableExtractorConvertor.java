package com.ruoyi.project.system.parse.parse.extractor;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.parse.domain.ParseTableConfig;
import com.ruoyi.project.system.parse.parse.domain.TableMatchMethodEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenl
 */
public class TableExtractorConvertor {

    public static List<Pair<String, TableParseRule<?>>> convert(List<ParseTableConfig> tableConfigList) {
        List<Pair<String, TableParseRule<?>>> tableParseRuleList = new ArrayList<>();
        for (ParseTableConfig tableConfig : tableConfigList) {
            if (tableConfig.getMatchMethod().intValue() == TableMatchMethodEnum.KV.getCode()) {
                tableParseRuleList.add(Pair.of(tableConfig.getConfigDesc(), buildMapExtractor(tableConfig)));
            } else if (tableConfig.getMatchMethod().intValue() == TableMatchMethodEnum.LIST.getCode()) {
                tableParseRuleList.add(Pair.of(tableConfig.getConfigDesc(),buildListExtractor(tableConfig)));
            }
        }
        return tableParseRuleList;
    }

    public static TableParseRule buildMapExtractor(ParseTableConfig tableConfig) {
        return new MapExtractor(getConditionArr(tableConfig), -1);
    }

    public static TableParseRule buildListExtractor(ParseTableConfig tableConfig) {
        return new ListExtractor(getConditionArr(tableConfig),
                tableConfig.getExpectationRow().intValue(), tableConfig.getInterpretConditions(),
                tableConfig.getIsMergeRow(), tableConfig.getIsMergeSameTitle());
    }

    private static String[] getConditionArr(ParseTableConfig tableConfig) {
        String conditions = tableConfig.getConditions();
        return Convert.toStrArray(conditions);
    }
}
