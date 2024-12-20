package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.domain.Enum.ParseTypeEnum;
import com.ruoyi.project.parse.domain.Enum.TableMatchMethodEnum;
import com.ruoyi.project.parse.domain.TableTypeEnum;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author chenl
 */
@Data
public class ExtractorConfig {

    private ParseTypeEnum parseType;
    private TableTypeEnum tableType;
    private TableMatchMethodEnum tableMatchMethod;

    private final String[] conditions;
    private final int expectParseRowSize;
    private final String interpretConditions;

    private final boolean isOpenThMultipleRowMatch;
    private final int thMultipleRowNumber;
    private final boolean isMergeRow;
    private final boolean isMergeSameTitle;
    private final boolean isRemoveEmptyRow;
    private final boolean isSmartHandle;
    private final boolean isKvTableOptimization;
    private final boolean isHandleUnit;

    private final Pattern textPattern;

    private ExtractorConfig(Builder builder) {
        this.parseType = builder.parseType;
        this.tableType = builder.tableType;
        this.tableMatchMethod = builder.tableMatchMethod;
        this.conditions = builder.conditions;
        this.expectParseRowSize = builder.expectParseRowSize;
        this.interpretConditions = builder.interpretConditions;
        this.isMergeRow = builder.isMergeRow;
        this.isMergeSameTitle = builder.isMergeSameTitle;
        this.isRemoveEmptyRow = builder.isRemoveEmptyRow;
        this.isSmartHandle = builder.isSmartHandle;
        this.isKvTableOptimization = builder.isKvTableOptimization;
        this.textPattern = builder.textPattern;
        this.isOpenThMultipleRowMatch = builder.isOpenThMultipleRowMatch;
        this.thMultipleRowNumber = builder.thMultipleRowNumber;
        this.isHandleUnit = builder.isHandleUnit;
    }

    public static class Builder {
        private ParseTypeEnum parseType;
        private TableTypeEnum tableType;
        private TableMatchMethodEnum tableMatchMethod;

        private String[] conditions;
        private int expectParseRowSize;
        private String interpretConditions;

        private boolean isHandleUnit = true;
        private boolean isMergeRow;
        private boolean isMergeSameTitle;
        private boolean isOpenThMultipleRowMatch = false;
        private int thMultipleRowNumber = 2;
        private boolean isRemoveEmptyRow = true;
        private boolean isSmartHandle = false;
        private boolean isKvTableOptimization = true;

        private Pattern textPattern;

        public Builder setParseType(ParseTypeEnum parseType) {
            this.parseType = parseType;
            return this;
        }

        public Builder setTableType(TableTypeEnum tableType) {
            this.tableType = tableType;
            return this;
        }

        public Builder setTableMatchMethod(TableMatchMethodEnum tableMatchMethod) {
            this.tableMatchMethod = tableMatchMethod;
            return this;
        }

        public Builder setConditions(String[] conditions) {
            this.conditions = conditions;
            return this;
        }

        public Builder setExpectParseRowSize(int expectParseRowSize) {
            this.expectParseRowSize = expectParseRowSize;
            return this;
        }

        public Builder setInterpretConditions(String interpretConditions) {
            this.interpretConditions = interpretConditions;
            return this;
        }

        public Builder setIsHandleUnit(boolean isHandleUnit) {
            this.isHandleUnit = isHandleUnit;
            return this;
        }

        public Builder setIsMergeRow(boolean isMergeRow) {
            this.isMergeRow = isMergeRow;
            return this;
        }

        public Builder setIsOpenThMultipleRowMatch(boolean isOpenThMultipleRowMatch) {
            this.isOpenThMultipleRowMatch = isOpenThMultipleRowMatch;
            return this;
        }

        public Builder setThMultipleRowNumber(int thMultipleRowNumber) {
            this.thMultipleRowNumber = thMultipleRowNumber;
            return this;
        }

        public Builder setIsMergeSameTitle(boolean isMergeSameTitle) {
            this.isMergeSameTitle = isMergeSameTitle;
            return this;
        }

        public Builder setIsRemoveEmptyRow(boolean isRemoveEmptyRow) {
            this.isRemoveEmptyRow = isRemoveEmptyRow;
            return this;
        }

        public Builder setIsSmartHandle(boolean isSmartHandle) {
            this.isSmartHandle = isSmartHandle;
            return this;
        }

        public Builder setIsKvTableOptimization(boolean isKvTableOptimization) {
            this.isKvTableOptimization = isKvTableOptimization;
            return this;
        }

        public Builder setTextPattern(Pattern textPattern) {
            this.textPattern = textPattern;
            return this;
        }

        public ExtractorConfig build() {
            return new ExtractorConfig(this);
        }
    }

    public ExtractorConfig(TableTypeEnum tableType, TableMatchMethodEnum tableMatchMethod,
                           String[] conditions, int expectParseRowSize, String interpretConditions,
                           boolean isMergeRow, boolean isMergeSameTitle, boolean isRemoveEmptyRow,
                           boolean isSmartHandle, boolean isKvTableOptimization, boolean isOpenThMultipleRowMatch,
                           int thMultipleRowNumber, boolean isHandleUnit) {
        this(new Builder()
                .setParseType(ParseTypeEnum.TABLE)
                .setTableType(tableType)
                .setTableMatchMethod(tableMatchMethod)
                .setConditions(conditions)
                .setExpectParseRowSize(expectParseRowSize)
                .setInterpretConditions(interpretConditions)
                .setIsMergeRow(isMergeRow)
                .setIsHandleUnit(isHandleUnit)
                .setIsMergeSameTitle(isMergeSameTitle)
                .setIsRemoveEmptyRow(isRemoveEmptyRow)
                .setIsSmartHandle(isSmartHandle)
                .setIsOpenThMultipleRowMatch(isOpenThMultipleRowMatch)
                .setThMultipleRowNumber(thMultipleRowNumber)
                .setIsKvTableOptimization(isKvTableOptimization));
    }

    public ExtractorConfig(Pattern textPattern) {
        this(new Builder()
                .setParseType(ParseTypeEnum.TEXT)
                .setTextPattern(textPattern));
    }
}