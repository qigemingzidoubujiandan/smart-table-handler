package com.ruoyi.project.parse.domain;

/**
 * cell
 */
public interface Cell<T, R> {

    /**
     * 获取文本
     */
    String text();

    /**
     * 设置文本
     */
    void setText(T t);


    /**
     * 扩展信息
     */
    R ext();

    /**
     * 设置扩展信息
     */
    void setExt(R r);

}
