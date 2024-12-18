package com.ruoyi.project.parse.extractor.unit;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.project.parse.util.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单位提取转换器 eg:6,666.66万元--->66666600.00
 *
 * @author jianwei
 * @date 2022/10/31
 */
public class UnitExtractConverter {

    public static final Pattern AMOUNT_UNIT_PATTERN2 = Pattern.compile(
            "(\\-|\\+){0,1}(([0-9]{1,3}(,[0-9]{3})*(.(([0-9]{3},)*[0-9]{1,})){0,1})|([0-9]+(.[0-9]+){0,1}))([\\u4e00-\\u9fa5]{1,3})");
    public static final Pattern AMOUNT_UNIT_PATTERN3 = Pattern.compile("^-?\\d+(,\\d{3})*(\\.\\d{1,6})?$");
    public static final Pattern CHINESE_BEGIN_PATTERN = Pattern.compile("^([\u4e00-\u9fa5A-Z]|\\d+\\.[\u4e00-\u9fa5])");
    public static final Pattern CHINESE_YUAN_END_PATTERN = Pattern.compile("(.*?)(元|份|港币|英镑)$");

    public static String handleAmountUnit(String txt) {
        if (Objects.isNull(txt)) {
            return null;
        }
        txt = trim(txt);
        String unitStr = amountUnitExtract(txt);
        if (StringUtils.isBlank(unitStr)) {
            if (isNumber(txt)) {
                return UnitConvert.format(txt);
            }
            return txt;
        }
        Function<String, String> fun = UnitConvert.getAmountConvertFactory().get(UnitConvert.Unit.getByUnit(unitStr));
        if (Objects.isNull(fun) || CHINESE_BEGIN_PATTERN.matcher(txt).find()
                || !ReUtil.isMatch(CHINESE_YUAN_END_PATTERN, txt)) {
            return txt;
        }
        return fun.apply(StringUtils.endsWith(txt, "元/份") ? txt : txt.replaceAll(unitStr, ""));
    }

    public static String trim(String txt) {
        txt = txt.replace("\u00a0", "");
        txt = txt.replace(" ", "");
        return txt;
    }

    public static String amountUnitExtract(String text) {
        text = text.replace("\u00a0", "");
        Matcher matcher = AMOUNT_UNIT_PATTERN2.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(10);
            if (StringUtils.isNotBlank(group)) {
                if (StrUtil.containsAny(group, "份", "元", "港币", "英镑")) {
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是纯数字
     */
    public static boolean isNumber(String text) {
        if (StrUtil.isEmpty(text)) {
            return false;
        }
        Matcher matcher = AMOUNT_UNIT_PATTERN3.matcher(text);
        return matcher.find();
    }

    public static String convertNumberStr(String txt) {
        return convertNumberStr(txt, null);
    }

    public static String convertNumberStr(String txt, Integer scale) {
        return Optional.ofNullable(convertNumber(txt, scale)).map(BigDecimal::toPlainString).orElse(null);
    }

    public static BigDecimal convertNumber(String txt, Integer scale) {
        String amountStr = handleAmountUnit(txt);

        return Optional.ofNullable(amountStr).map(a -> {
            if (isNumber(amountStr)) {
                if (scale != null && scale == 4) {
                    return BigDecimalUtil.retain4Decimals(amountStr);
                }
                return BigDecimalUtil.retain2Decimals(amountStr);
            }
            return null;
        }).orElse(null);
    }
}
