package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.domain.TableMatchMethodEnum;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class ExtractorConfig {

    private ParseTypeEnum parseType;
    private TableMatchMethodEnum tableMatchMethod;

    private final String[] conditions;
    private final int expectParseRowSize;
    private final String interpretConditions;

    private final boolean isMergeRow;
    private final boolean isMergeSameTitle;

    private final Pattern textPattern;

    public ExtractorConfig(TableMatchMethodEnum tableMatchMethod, String[] conditions, int expectParseRowSize, String interpretConditions, boolean isMergeRow, boolean isMergeSameTitle) {
        this.conditions = conditions;
        this.expectParseRowSize = expectParseRowSize;
        this.interpretConditions = interpretConditions;
        this.isMergeRow = isMergeRow;
        this.isMergeSameTitle = isMergeSameTitle;
        this.textPattern = null;
        this.tableMatchMethod = tableMatchMethod;
        this.parseType = ParseTypeEnum.TABLE;
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
