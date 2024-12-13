package com.ruoyi.project.parse.domain;

import java.util.List;

/**
 * 表格
 */
@SuppressWarnings("rawtypes")
public interface Table {


    /**
     * 获取所有的表头
     */
    List<? extends Cell> getTh();

    FileTypeEnum source();

    /**
     * 是否抽取过
     */
    boolean isExtracted();

    /**
     * 所有的cell
     */
    List<List<Cell>> getData();

    /**
     * 设置抽取过
     */
    void setExtracted(boolean b);

    /**
     * 删除表头
     */
    void passTh();

    void setNotEmptyData(List<List<Cell>> data);
}
