package com.ruoyi.project.system.parse.parse.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.ruoyi.project.system.parse.parse.convert.UnitExtractConverter.isNumber;

@Slf4j
public class BigDecimalUtil {

    public static BigDecimal retain2Decimals(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.warn("decimal convert error! value:{}", value);
            return null;
        }
    }


    public static BigDecimal retain2DecimalsOrZero(String value) {
        BigDecimal val = retain2Decimals(value);
        return Objects.isNull(val) ? BigDecimal.ZERO : val;
    }

    public static BigDecimal retain4Decimals(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("decimal convert error! value:{}", value, e);
            return null;
        }
    }

    /**
     * 计算前一个数占后一个数的百分比例.
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @param scale 精度
     * @param roundingMode 保留小数的模式
     */
    public static BigDecimal calcPercentageRate(BigDecimal dividend, BigDecimal divisor, int scale, int roundingMode) {
        if (dividend == null || divisor == null) {
            return BigDecimal.ZERO;
        }
        // 除数为0时，比例返回为0
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return dividend.multiply(new BigDecimal("100")).divide(divisor, scale, roundingMode);
    }

    /**
     * 计算前一个数占后一个数的百分比例，精度为2，模式为直接截断.
     *
     * @param dividend 被除数
     * @param divisor 除数
     */
    public static BigDecimal calcPercentageRate(BigDecimal dividend, BigDecimal divisor) {
        return calcPercentageRate(dividend, divisor, 2, BigDecimal.ROUND_DOWN);
    }

    public static String calcPercentageRateEasy(String dividend, String divisor) {
        if (!isNumber(dividend) || !isNumber(divisor)) {
            return "0.00";
        }
        return calcPercentageRate(new BigDecimal(dividend), new BigDecimal(divisor)).setScale(
                2, RoundingMode.HALF_UP
        ).toPlainString();
    }

    /**
     * 计算总数*百分比
     *
     * @param total 总数
     * @param percentage 百分比
     */
    public static BigDecimal multiplyPercentage(BigDecimal total, BigDecimal percentage) {
        return total.multiply(percentage).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal multiplyPercentage(String total, String percentage) {
        if (StrUtil.isEmpty(total) || StrUtil.isEmpty(percentage) || "-".equals(percentage)) {
            return BigDecimal.ZERO;
        }
        String percentage1 = percentage.replace("%", "");
        return multiplyPercentage(new BigDecimal(total), new BigDecimal(percentage1));
    }

    public static String multiplyPercentageEasy(String total, String percentage) {
        return multiplyPercentage(total, percentage).setScale(
                2, RoundingMode.HALF_UP
        ).toPlainString();
    }
}
