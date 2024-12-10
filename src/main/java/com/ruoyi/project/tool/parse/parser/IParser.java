package com.ruoyi.project.tool.parse.parser;

public interface IParser<T, R> {

    /**
     * 解析
     */
    R parse(T t);
}
