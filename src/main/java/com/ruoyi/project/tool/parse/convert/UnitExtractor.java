package com.ruoyi.project.tool.parse.convert;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.tool.parse.util.TableUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jianwei
 * @date 2022/10/11
 * 金额、份额单位提取器
 */
public class UnitExtractor {

    /**
     * 提取表格表头金额、份额单位的正则
     */
    static final Pattern AMOUNT_UNIT_PATTERN = Pattern.compile("(?<=[（|(]).*()元.*(?=[）|)])");
    static final Pattern AMOUNT_UNIT_PATTERN_FEN = Pattern.compile("(?<=[（|(]).*()份.*(?=[）|)])");
    static final Pattern AMOUNT_UNIT_PATTERN_YI = Pattern.compile("(?<=[（|(]).*()亿.*(?=[）|)])");
    private static final List<Function<String, String>> UNIT_FUNCTION_LIST;

    static {
        UNIT_FUNCTION_LIST = ListUtil.of(
                text -> dealWithUnit(AMOUNT_UNIT_PATTERN, "元", text),
                text -> dealWithUnit(AMOUNT_UNIT_PATTERN_FEN, "份", text),
                text -> dealWithUnit(AMOUNT_UNIT_PATTERN_YI, "亿", text)
        );
    }

    private UnitExtractor() {}

    private static String dealWithUnit(Pattern pattern, String unit, String text) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return null;
        }
        String group = matcher.group();
        if (CharSequenceUtil.isNotEmpty(group)) {
            if (group.contains(unit)) {
                group = obtainUnit(group);
                return group.substring(0, group.indexOf(unit) + 1);
            }
        }
        return group;
    }

    public static String amountUnitExtract(String txt) {
        return UNIT_FUNCTION_LIST.stream().map((fun) -> fun.apply(TableUtil.format(txt)))
                .filter(StringUtils::isNotBlank).findFirst().orElse(null);
    }

    /**
     * 自定义规则抽取单位
     */
    public static String amountUnitExtract(List<Function<String, String>> unitFunList, String txt) {
        return unitFunList.stream().map(fun -> fun.apply(TableUtil.format(txt))).filter(StringUtils::isNotBlank)
                .findFirst().orElse(null);
    }

    /**
     * 获取单位
     *
     * @param unit
     * @return
     */
    public static String obtainUnit(String unit) {
        return Arrays.stream(UnitConvert.Unit.values()).map(UnitConvert.Unit::getUnit).filter(unit::contains)
                .max(Comparator.comparingInt(String::length)).orElse(unit);
    }
}
