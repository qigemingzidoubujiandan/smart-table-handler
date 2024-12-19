package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.domain.Enum.ParseTypeEnum;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.domain.TableTypeEnum;

/**
 * @author chenl
 */
public class ExtractorConvertor {

    @SuppressWarnings("unchecked")
    public static <T, R extends ExtractedResult> IExtractor<T, R> createExtractor(ExtractorConfig extractorConfig) {
        ParseTypeEnum parseType = extractorConfig.getParseType();
        if (parseType.equals(ParseTypeEnum.TABLE)) {
            switch (TableTypeEnum.fromCode(extractorConfig.getTableType().getCode())) {
                case KV:
                    return (IExtractor<T, R>) new MapExtractor(extractorConfig);
                case LIST:
                    return (IExtractor<T, R>) new ListExtractor(extractorConfig);
                default:
                    throw new IllegalArgumentException("Unsupported table type");
            }
        } else if (parseType.equals(ParseTypeEnum.TEXT)) {
            return (IExtractor<T, R>) new BaseTextExtractor(extractorConfig);
        } else {
            throw new IllegalArgumentException("Unsupported parse type");
        }
    }

}