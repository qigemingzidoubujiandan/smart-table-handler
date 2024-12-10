package com.ruoyi.project.tool.parse.domain;

import lombok.Data;

import java.util.List;

@SuppressWarnings("rawtypes")
@Data
public class WordTable implements Table {

    /**
     * 是否被抽取过
     */
    private boolean extracted;

    private List<List<Cell>> data;

    @Override
    public List<List<Cell>> getData() {
        return data;
    }

    @Override
    public void passTh() {
        if (data.size() > 0) {
            data.remove(0);
        } else {
            throw new RuntimeException("当前表格不可删除指定数据");
        }
    }

    @Override
    public void setNotEmptyData(List<List<Cell>> data) {
        this.data = data;
    }

    @Override
    public List<Cell> getTh() {
        return data.size() > 0 ? data.get(0) : null;
    }

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.WORD;
    }
}
