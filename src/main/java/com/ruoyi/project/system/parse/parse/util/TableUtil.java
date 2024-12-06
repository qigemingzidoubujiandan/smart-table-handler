package com.ruoyi.project.system.parse.parse.util;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TableUtil {

    public static String format(String text) {
        if (Objects.isNull(text)) {
            return null;
        }
        return text.replaceAll("\\s*|\r|\n|\t", "").trim();
    }

    public static String removeBrackets(String text) {
        if (Objects.isNull(text)) {
            return null;
        }
        return text.replaceAll("[\\（\\(\\)\\）]", "").trim();
    }

    /**
     * 拆分pdf表格 eg：1行4列 拆分成2列 返回 2行2列
     *
     * @param list 原始数据
     * @param size 拆分后每段（行）长度
     */
    public static <T> List<List<T>> split(List<List<T>> list, Integer size) {
        List<List<T>> rstList = new ArrayList<>();
        list.forEach(l -> {
            List<List<T>> split = ListUtil.split(l, size);
            rstList.addAll(split);
        });
        return rstList;
    }

}
