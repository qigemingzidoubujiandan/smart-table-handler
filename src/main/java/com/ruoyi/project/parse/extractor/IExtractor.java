package com.ruoyi.project.parse.extractor;

/**
 * 处理
 */
public interface IExtractor<T, R> {

    /**
     * 抽取
     */
    R extract(T t);
}
