package com.ruoyi.project.system.parse.parse.extractor;

/**
 * 处理
 */
public interface IExtractor<T, R> {

    /**
     * 抽取
     */
    R extract(T t);
}
