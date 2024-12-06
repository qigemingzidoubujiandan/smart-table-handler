package com.ruoyi.project.system.parse.parse.rule;


/**
 * pdf table匹配  适用于匹配整个table的匹配
 */
public  interface TableParseRule<T> {

    /**
     * 期望解析的行数
     */
    int expectParseRowSize();

    /**
     * 解析条件
     * 表头 或者 表格首列值
     */
    String[] condition();

    /**
     * 是否解析完成
     * 目前仅仅在pdf表格有用到
     */
    boolean parsed();

    /**
     * 解析后填充匹配的数据
     */
    void fillMatchedData(T t);


    /**
     * 获取匹配得数据
     */
    T matchedData();


}
