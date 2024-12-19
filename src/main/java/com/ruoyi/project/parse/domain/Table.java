package com.ruoyi.project.parse.domain;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.project.parse.domain.Enum.FileTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 表格
 */
@SuppressWarnings("rawtypes")
public abstract class Table {

    /**
     * 是否被抽取过
     */
    @Setter
    @Getter
    private boolean extracted;
    private List<List<Cell>> data;

    /**
     * 获取所有的表头
     */
    public List<? extends Cell> getTh() {
        return !data.isEmpty() ? data.get(0) : null;
    }

    public abstract FileTypeEnum source();

    /**
     * 所有的cell
     */
    public List<List<Cell>> getData() {
        return data;
    }

    public void setData(List<List<Cell>> data) {
        if (CollUtil.isNotEmpty(data)) {
            this.data = data;
        }
    }
}
