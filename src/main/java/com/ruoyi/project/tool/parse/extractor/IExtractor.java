package com.ruoyi.project.tool.parse.extractor;

import com.ruoyi.project.tool.parse.domain.Table;

import java.util.List;

/**
 * 处理
 */
public interface IExtractor<T, R> {

    /**
     * 抽取
     */
    R extract(T t);
}
