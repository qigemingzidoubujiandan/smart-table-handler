package com.ruoyi.project.parse.extractor;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.domain.TableMatchMethodEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author chenl
 */
public class ExtractorConvertor {

    public static <T, R> Pair<String, ? extends IExtractor<?, ?>> createExtractor(ParseConfig parseConfig) {
        Pair<String, ? extends IExtractor<?, ?>> pair = null;
        if (parseConfig.getConfigType().intValue() == ParseTypeEnum.TABLE.getCode()) {
            if (parseConfig.getTableType().intValue() == TableMatchMethodEnum.KV.getCode()) {
                pair = ExtractorConvertor.convertToMapExtractor(parseConfig);
            } else if (parseConfig.getTableType().intValue() == TableMatchMethodEnum.LIST.getCode()) {
                pair = ExtractorConvertor.convertToListExtractor(parseConfig);
            }
        } else {
            pair = ExtractorConvertor.convertToTextExtractor(parseConfig);
        }
        return pair;
    }

    // 泛型工厂方法接口
    @FunctionalInterface
    public interface ExtractorFactory<T, R> {
        IExtractor<T, R> create(ParseConfig config);
    }

    // 创建提取器并返回带有具体类型的 Pair
    private static <T, R> Pair<String, IExtractor<T, R>> createExtractor(ParseConfig config, ExtractorFactory<T, R> factory) {
        IExtractor<T, R> extractor = factory.create(config);
        return Pair.of(config.getParseDesc(), extractor);
    }

    public static Pair<String, IExtractor<List<Table>, Map<String, String>>> convertToMapExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildMapExtractor);
    }

    public static Pair<String, IExtractor<List<Table>, List<List<String>>>> convertToListExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildListExtractor);
    }

    public static Pair<String, IExtractor<String, String>> convertToTextExtractor(ParseConfig config) {
        return createExtractor(config, ExtractorConvertor::buildTextExtractor);
    }

    // 提取器的具体实现
    private static IExtractor<String, String> buildTextExtractor(ParseConfig config) {
        return new BaseTextExtractor(Pattern.compile(config.getTextRegExpression()));
    }

    public static IExtractor<List<Table>, Map<String, String>> buildMapExtractor(ParseConfig tableConfig) {
        return new MapExtractor(getConditionArr(tableConfig), -1);
    }

    public static IExtractor<List<Table>, List<List<String>>> buildListExtractor(ParseConfig tableConfig) {
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

    // 批量转换配置列表
    public static List<Pair<String, ? extends IExtractor<?, ?>>> convert(List<ParseConfig> tableConfigList) {
        List<Pair<String, ? extends IExtractor<?, ?>>> extractorList = new ArrayList<>();
        for (ParseConfig tableConfig : tableConfigList) {
            if (tableConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.KV.getCode()) {
                extractorList.add(convertToMapExtractor(tableConfig));
            } else if (tableConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.LIST.getCode()) {
                extractorList.add(convertToListExtractor(tableConfig));
            }
        }
        return extractorList;
    }
}