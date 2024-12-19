package com.ruoyi.project.parse.extractor.unit;


import cn.hutool.core.text.CharSequenceUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额转换
 */
@Slf4j
public class UnitConvert {

    @Getter
    private static final EnumMap<Unit, Function<String, String>> amountConvertFactory = new EnumMap<>(Unit.class);;

//    static {
//        amountConvertFactory = new EnumMap<>(Unit.class);
//        amountConvertFactory.put(Unit.YUAN, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_YUAN, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_YUAN, UnitConvert::convert_yi_yuan);
//        amountConvertFactory.put(Unit.YI, UnitConvert::convert_yi_yuan);
//        amountConvertFactory.put(Unit.WAN, UnitConvert::convert_wan_yuan);
//
//        // TODO 美元暂默认和元处理一致。否则入库为null
//        amountConvertFactory.put(Unit.MEI_YUAN, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_MEI_YUAN, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_MEI_YUAN, UnitConvert::convert_yi_yuan);
//
//        amountConvertFactory.put(Unit.GANG_BI, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_GANG_BI, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_GANG_BI, UnitConvert::convert_yi_yuan);
//
//        amountConvertFactory.put(Unit.YING_BANG, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_YING_BANG, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_YING_BANG, UnitConvert::convert_yi_yuan);
//
//        amountConvertFactory.put(Unit.AUD, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_AUD, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_AUD, UnitConvert::convert_yi_yuan);
//
//        amountConvertFactory.put(Unit.EURO, UnitConvert::convert_yuan);
//        amountConvertFactory.put(Unit.WAN_EURO, UnitConvert::convert_wan_yuan);
//        amountConvertFactory.put(Unit.YI_EURO, UnitConvert::convert_yi_yuan);
//
//        amountConvertFactory.put(Unit.FEN, UnitConvert::convert_fen);
//        amountConvertFactory.put(Unit.WAN_FEN, UnitConvert::convert_wan_fen);
//        amountConvertFactory.put(Unit.YI_FEN, UnitConvert::convert_yi_fen);
//    }

    // 初始化静态工厂时直接使用枚举常量进行映射
    static {
        for (Unit unit : Unit.values()) {
            if (!unit.equals(Unit.DEFAULT_IDENTITY)) {
                amountConvertFactory.put(unit, amount -> convert(amount, unit));
            }
        }
    }

    //    static final Pattern AMOUNT_PATTERN = Pattern.compile("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$");
    public static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "^(\\-|\\+){0,1}(([0-9]{1,3}(,[0-9]{3})*(.(([0-9]{3},)*[0-9]{1,})){0,1})|([0-9]+(.[0-9]+){0,1}))$");

    public static String convert_yuan(String amount) {
        return convert(amount, Unit.YUAN);
    }

    public static String convert_wan_yuan(String amount) {
        return convert(amount, Unit.WAN_YUAN);
    }

    public static String convert_yi_yuan(String amount) {
        return convert(amount, Unit.YI_YUAN);
    }

    public static String convert_fen(String amount) {
        return convert(amount, Unit.FEN);
    }

    public static String convert_wan_fen(String amount) {
        return convert(amount, Unit.WAN_FEN);
    }

    public static String convert_yi_fen(String amount) {
        return convert(amount, Unit.YI_FEN);
    }

    /**
     * 默认消除千分位，值不变
     */
    public static String convert_default(String amount) {
        return convert(amount, Unit.DEFAULT_IDENTITY);
    }

    private static String convert(String amountStr, Unit unit) {
        if (CharSequenceUtil.isEmpty(amountStr) || Objects.isNull(unit)) {
            return amountStr;
        }
        Matcher matcher = AMOUNT_PATTERN.matcher(amountStr);
        String matchStr = null;
        if (matcher.find()) {
            matchStr = matcher.group();
        }
        // 没有匹配金额的返回原str
        if (CharSequenceUtil.isEmpty(matchStr)) {
            return amountStr;
        }
        return new BigDecimal(format(matchStr)).multiply(unit.getMultiplicand()).toPlainString();
    }

    public static String format(String amount) {
        Number number;
        try {
            number = new DecimalFormat().parse(amount);
        } catch (ParseException e) {
            log.error("金额解析失败：{}, amount:{}", e.getMessage(), amount);
            return amount;
        }
        NumberFormat instance = NumberFormat.getNumberInstance();
        instance.setGroupingUsed(false);
        instance.setMinimumFractionDigits(2);
        instance.setMaximumFractionDigits(6);
        instance.setRoundingMode(RoundingMode.HALF_UP);
        return instance.format(number);
    }

    public enum Unit {
        YUAN("元", new BigDecimal(1)),
        WAN_YUAN("万元", new BigDecimal(10000)),
        YI_YUAN("亿元", new BigDecimal(100000000)),

        WAN("万", new BigDecimal(10000)),
        YI("亿", new BigDecimal(100000000)),
        MEI_YUAN("美元", new BigDecimal(1)),
        WAN_MEI_YUAN("万美元", new BigDecimal(10000)),
        YI_MEI_YUAN("亿美元", new BigDecimal(100000000)),
        FEN("份", new BigDecimal(1)),
        WAN_FEN("万份", new BigDecimal(10000)),
        YI_FEN("亿份", new BigDecimal(100000000)),

        GANG_BI("港币", new BigDecimal(1)),
        WAN_GANG_BI("万港币", new BigDecimal(10000)),
        YI_GANG_BI("亿港币", new BigDecimal(100000000)),

        YING_BANG("英镑", new BigDecimal(1)),
        WAN_YING_BANG("万英镑", new BigDecimal(10000)),
        YI_YING_BANG("亿英镑", new BigDecimal(100000000)),

        EURO("欧元", new BigDecimal(1)),
        WAN_EURO("万欧元", new BigDecimal(10000)),
        YI_EURO("亿欧元", new BigDecimal(100000000)),

        AUD("澳元", new BigDecimal(1)),
        WAN_AUD("万澳元", new BigDecimal(10000)),
        YI_AUD("亿澳元", new BigDecimal(100000000)),

        DEFAULT_IDENTITY("default_identity", new BigDecimal(1));
        private final String unit;

        private final BigDecimal multiplicand;

        Unit(String unit, BigDecimal multiplicand) {
            this.unit = unit;
            this.multiplicand = multiplicand;
        }

        public String getUnit() {
            return unit;
        }

        public static Unit getByUnit(String str) {
            for (Unit value : Unit.values()) {
                if (value.unit.equals(str)) {
                    return value;
                }
            }
            return null;
        }

        public BigDecimal getMultiplicand() {
            return multiplicand;
        }
    }

}
