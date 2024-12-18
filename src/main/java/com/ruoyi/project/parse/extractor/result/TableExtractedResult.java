package com.ruoyi.project.parse.extractor.result;

import java.util.List;

/**
 * @author chenl
 */
public class TableExtractedResult extends ExtractedResult {
    private final List<List<String>> tableData;

    public TableExtractedResult(List<List<String>> tableData) {
        this.tableData = tableData;
    }

    public List<List<String>> getTableData() {
        return tableData;
    }

    @Override
    public String getType() {
        return "TABLE";
    }

    @Override
    public String toString() {
        return "TableExtractedResult{" + "tableData=" + tableData + '}';
    }
}

