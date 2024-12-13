package com.ruoyi.project.parse.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jianwei
 * @date 2022/10/24
 */
public class RegUtil {

    /**
     * @param text 待匹配的数据
     * @param pattern pattern
     * @param groupIndex 组的索引 默认值为1
     */
    public static String applyReg(String text, Pattern pattern, Integer groupIndex) {
        groupIndex = groupIndex == null ? 1 : groupIndex;
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    /**
     * 循环匹配，返回最后匹配到的数据
     */
    public static String applyLoopReg(String text, Pattern pattern, Integer groupIndex) {
        groupIndex = groupIndex == null ? 1 : groupIndex;
        String ret = null;
        if (Objects.isNull(text)) {
            return null;
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ret = matcher.group(groupIndex);
        }
        return ret;
    }

    public static String applyRegOrDefault(String text, Pattern pattern, Integer groupIndex) {
        groupIndex = groupIndex == null ? 1 : groupIndex;
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return text;
    }

    public static String getFirstAllGroups(String text, Pattern pattern) {
        List<String> allGroups = ReUtil.getAllGroups(pattern, text, false);
        if (CollUtil.isEmpty(allGroups)) {
            return null;
        }
        return allGroups.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }
}

