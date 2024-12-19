package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.domain.Enum.ParseTypeEnum;
import com.ruoyi.project.parse.domain.Enum.TableMatchMethodEnum;
import com.ruoyi.project.parse.domain.TableTypeEnum;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class ExtractorConfig {

    private ParseTypeEnum parseType;
    private TableTypeEnum tableType;
    private TableMatchMethodEnum tableMatchMethod;

    private final String[] conditions;
    private final int expectParseRowSize;
    private final String interpretConditions;

    private final boolean isMergeRow;
    private final boolean isMergeSameTitle;
    private boolean isRemoveEmptyRow = true;


    private final Pattern textPattern;

    public ExtractorConfig(TableTypeEnum tableType, TableMatchMethodEnum tableMatchMethod, String[] conditions, int expectParseRowSize, String interpretConditions, boolean isMergeRow, boolean isMergeSameTitle, boolean isRemoveEmptyRow) {
        this.conditions = conditions;
        this.expectParseRowSize = expectParseRowSize;
        this.interpretConditions = interpretConditions;
        this.isMergeRow = isMergeRow;
        this.isMergeSameTitle = isMergeSameTitle;
        this.isRemoveEmptyRow = isRemoveEmptyRow;
        this.textPattern = null;
        this.tableType = tableType;
        this.parseType = ParseTypeEnum.TABLE;
        this.tableMatchMethod = tableMatchMethod;
    }

    public ExtractorConfig(Pattern textPattern) {
        this.parseType = ParseTypeEnum.TEXT;
        this.conditions = null;
        this.expectParseRowSize = -1;
        this.interpretConditions = null;
        this.isMergeRow = false;
        this.isMergeSameTitle = false;
        this.textPattern = textPattern;
    }


}
