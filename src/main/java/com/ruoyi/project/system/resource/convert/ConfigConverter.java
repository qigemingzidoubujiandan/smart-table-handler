package com.ruoyi.project.system.resource.convert;

import java.util.regex.Pattern;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.domain.TableTypeEnum;
import com.ruoyi.project.parse.extractor.ExtractorConfig;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;

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

        if (configType.equals(ParseTypeEnum.TABLE.getCode())) { // 表格配置
            return new ExtractorConfig(
                    TableTypeEnum.get(parseConfig.getTableType()),
                    Convert.toStrArray(parseConfig.getTableConditions()),
                    parseConfig.getTableExpectationRow().intValue(),
                    parseConfig.getTableInterpretConditions(),
                    "1".equals(parseConfig.getTableIsMergeRow()),
                    "1".equals(parseConfig.getTableIsMergeSameTitle()));
        } else if (configType.equals(ParseTypeEnum.TEXT.getCode())) { // 文本配置
            String textRegExpression = parseConfig.getTextRegExpression();
            if (textRegExpression == null || textRegExpression.isEmpty()) {
                throw new IllegalArgumentException("TextRegExpression cannot be null or empty for text configuration");
            }
            Pattern pattern = Pattern.compile(textRegExpression);
            return new ExtractorConfig(pattern);
        } else {
            throw new IllegalArgumentException("Unsupported config type: " + configType);
        }
    }
}
