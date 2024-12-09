package com.ruoyi.project.system.parse.parse.extractor;

import com.ruoyi.project.system.parse.parse.domain.Table;

import java.util.List;

/**
 * @author chenl
 */
public class TableParser {

    public static void execute(List<TableParseRule<?>> tableParseRules, List<? extends Table> tables ) {
        for (TableParseRule<?> parseRule : tableParseRules) {
            Object execute = parseRule.execute(tables);
        }
    }
}
