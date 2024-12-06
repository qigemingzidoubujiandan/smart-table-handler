package com.ruoyi.project.system.parse.parse.parser;

public interface IParser<T, R> {

    /**
     * 解析
     */
    R parse(T t);
}
