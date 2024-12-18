package com.ruoyi.project.parse.parser;

/**
 * @author chenl
 */
public interface IParser<T, R> {

    /**
     * 解析
     */
    R parse(T t);
}
