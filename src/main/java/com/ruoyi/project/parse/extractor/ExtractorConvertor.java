package com.ruoyi.project.parse.extractor;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.result.TableExtractedResult;
import com.ruoyi.project.parse.extractor.result.TextExtractedResult;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.domain.TableMatchMethodEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chenl
 */
public class ExtractorConvertor {

    // 泛型工厂方法接口
    @FunctionalInterface
    public interface ExtractorFactory<T, R extends ExtractedResult> {
        IExtractor<T, R> create(ParseConfig config);
    }
    public static Pair<String, ? extends IExtractor<?, ? extends ExtractedResult>> createExtractor(ParseConfig parseConfig) {
        if (parseConfig.getConfigType().intValue() == ParseTypeEnum.TABLE.getCode()) {
            switch (TableMatchMethodEnum.fromCode(parseConfig.getTableType().intValue())) {
                case KV:
                    return convertToMapExtractor(parseConfig);
                case LIST:
                    return convertToListExtractor(parseConfig);
                default:
                    throw new IllegalArgumentException("Unsupported table type: " + parseConfig.getTableType());
            }
        } else {
            return convertToTextExtractor(parseConfig);
        }
    }

    // 创建提取器并返回带有具体类型的 Pair
    private static <T, R extends ExtractedResult> Pair<String, IExtractor<T, R>> createExtractor(ParseConfig config, ExtractorFactory<T, R> factory) {
        IExtractor<T, R> extractor = factory.create(config);
        return Pair.of(config.getParseDesc(), extractor);
    }

    public static Pair<String, IExtractor<List<Table>, KVExtractedResult>> convertToMapExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildMapExtractor);
    }

    public static Pair<String, IExtractor<List<Table>, TableExtractedResult>> convertToListExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildListExtractor);
    }

    public static Pair<String, IExtractor<String, TextExtractedResult>> convertToTextExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildTextExtractor);
    }

    // 提取器的具体实现
    private static IExtractor<String, TextExtractedResult> buildTextExtractor(ParseConfig config) {
        return new BaseTextExtractor(Pattern.compile(config.getTextRegExpression()));
    }

    private static IExtractor<List<Table>, KVExtractedResult> buildMapExtractor(ParseConfig tableConfig) {
        return new MapExtractor(getConditionArr(tableConfig), -1);
    }

    private static IExtractor<List<Table>, TableExtractedResult> buildListExtractor(ParseConfig tableConfig) {
        return new ListExtractor(
                getConditionArr(tableConfig),
                tableConfig.getTableExpectationRow().intValue(),
                tableConfig.getTableInterpretConditions(),
                tableConfig.getTableIsMergeRow(),
                tableConfig.getTableIsMergeSameTitle()
        );
    }

    private static String[] getConditionArr(ParseConfig tableConfig) {
        String conditions = tableConfig.getTableConditions();
        return Convert.toStrArray(conditions);
    }

    public static List<Pair<String, ? extends IExtractor<?, ? extends ExtractedResult>>> convert(List<ParseConfig> tableConfigList) {
        return tableConfigList.stream()
                .map(config -> {
                    TableMatchMethodEnum matchMethod = TableMatchMethodEnum.fromCode(config.getTableMatchMethod().intValue());
                    switch (matchMethod) {
                        case KV:
                            return convertToMapExtractor(config);
                        case LIST:
                            return convertToListExtractor(config);
                        default:
                            throw new IllegalArgumentException("Unsupported table match method: " + config.getTableMatchMethod());
                    }
                })
                .collect(Collectors.toList());
    }
}