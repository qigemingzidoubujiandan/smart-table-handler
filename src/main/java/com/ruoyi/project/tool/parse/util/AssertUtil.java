package com.ruoyi.project.tool.parse.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

public class AssertUtil {

    public static void notBlank(String param, String message) {
        Assert.notBlank(param, () -> new RuntimeException(message));
    }

    public static void notEmpty(Object object, String message) {
        if (ObjectUtil.isEmpty(object)) {
            throw new RuntimeException(message);
        }
    }

    public static void isEmpty(Object object, String message) {
        if (ObjectUtil.isNotEmpty(object)) {
            throw new RuntimeException(message);
        }
    }

    public static void isNull(Object object, String message) {
        Assert.isNull(object, () -> new RuntimeException(message));
    }

    public static void notNull(Object object, String message) {
        Assert.notNull(object, () -> new RuntimeException(message));
    }

    public static void isTrue(boolean expression, String message) {
        Assert.isTrue(expression, () -> new RuntimeException(message));
    }

    public static void isFalse(boolean expression, String message) {
        Assert.isFalse(expression, () -> new RuntimeException(message));
    }

    public static void isMatch(String param, String pattern, String message) {
        isTrue(param.matches(pattern), message);
    }

    public static void eq(Object param1, Object param2, String message) {
        Assert.equals(param1, param2, () -> new RuntimeException(message));
    }
}
