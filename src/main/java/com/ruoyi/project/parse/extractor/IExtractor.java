package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.extractor.result.ExtractedResult;

/**
 * 处理
 * @author chenl
 */
public interface IExtractor<T, R extends ExtractedResult> {

    /**
     * 抽取
     */
    R extract(T t);
}
