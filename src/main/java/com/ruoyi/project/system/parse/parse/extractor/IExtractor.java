package com.ruoyi.project.system.parse.parse.extractor;

import com.ruoyi.project.system.parse.parse.domain.Table;

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
