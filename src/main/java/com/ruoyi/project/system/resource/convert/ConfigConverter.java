package com.ruoyi.project.system.resource.convert;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.parse.domain.Enum.ParseTypeEnum;
import com.ruoyi.project.parse.domain.Enum.TableMatchMethodEnum;
import com.ruoyi.project.parse.domain.TableTypeEnum;
import com.ruoyi.project.parse.extractor.ExtractorConfig;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.ruoyi.common.constant.Constants.YES;

/**
 * @author chenl
 */
public class ConfigConverter {

    /**
     * 将数据库表实体 ParseConfig 转换为 ExtractorConfig。
     *
     * @param parseConfig 数据库表实体
     * @return 对应的 ExtractorConfig
     */
    public static ExtractorConfig convertToExtractorConfig(ParseConfig parseConfig) {
        if (parseConfig == null) {
            throw new IllegalArgumentException("ParseConfig cannot be null");
        }

        Integer configType = parseConfig.getConfigType();
        if (configType == null) {
            throw new IllegalArgumentException("ConfigType in ParseConfig cannot be null");
        }

        switch (Objects.requireNonNull(ParseTypeEnum.get(configType))) {
            case TABLE:
                validateTableConfig(parseConfig);
                return createTableExtractorConfig(parseConfig);
            case TEXT:
                validateTextConfig(parseConfig);
                return createTextExtractorConfig(parseConfig);
            default:
                throw new IllegalArgumentException("Unsupported config type: " + configType);
        }
    }

    private static void validateTableConfig(ParseConfig parseConfig) {
        if (parseConfig.getTableType() == null || parseConfig.getTableMatchMethod() == null) {
            throw new IllegalArgumentException("TableType and TableMatchMethod in ParseConfig cannot be null");
        }
    }

    private static void validateTextConfig(ParseConfig parseConfig) {
        if (parseConfig.getTextRegExpression() == null || parseConfig.getTextRegExpression().isEmpty()) {
            throw new IllegalArgumentException("TextRegExpression cannot be null or empty for text configuration");
        }
    }

    private static ExtractorConfig createTableExtractorConfig(ParseConfig parseConfig) {
        return new ExtractorConfig.Builder()
                .setParseType(ParseTypeEnum.TABLE)
                .setTableType(TableTypeEnum.get(parseConfig.getTableType()))
                .setTableMatchMethod(TableMatchMethodEnum.get(parseConfig.getTableMatchMethod()))
                .setConditions(Convert.toStrArray(parseConfig.getTableConditions()))
                .setExpectParseRowSize(parseConfig.getTableExpectationRow())
                .setInterpretConditions(parseConfig.getTableInterpretConditions())
                .setThMultipleRowNumber(parseConfig.getThMultipleRowNumber())

                .setIsMergeRow(Objects.equals(YES, parseConfig.getTableIsMergeRow()))
                .setIsMergeSameTitle(Objects.equals(YES, parseConfig.getTableIsMergeSameTitle()))
                .setIsRemoveEmptyRow(Objects.equals(YES, parseConfig.getTableIsDelEmptyRow()))
                .setIsSmartHandle(Objects.equals(YES, parseConfig.getTableIsSmartHandle()))
                .setIsKvTableOptimization(Objects.equals(YES, parseConfig.getTableIsKvTableOptimization()))
                .setIsHandleUnit(Objects.equals(YES, parseConfig.getTableIsHandleUnit()))
                .build();
    }

    private static ExtractorConfig createTextExtractorConfig(ParseConfig parseConfig) {
        return new ExtractorConfig.Builder()
                .setParseType(ParseTypeEnum.TEXT)
                .setTextPattern(Pattern.compile(parseConfig.getTextRegExpression()))
                .build();
    }
}