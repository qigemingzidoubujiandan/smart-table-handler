package com.ruoyi.project.system.parse.parse.parser;

public abstract class AbstractTextParser<T> implements IParser<T, String> {

    /**
     * 解析
     */
    @Override
    public abstract String parse(T t);
}
