package com.ruoyi.project.parse.parser;

public abstract class AbstractTextParser<T> implements IParser<T, String> {

    /**
     * 解析
     */
    @Override
    public abstract String parse(T t);
}
