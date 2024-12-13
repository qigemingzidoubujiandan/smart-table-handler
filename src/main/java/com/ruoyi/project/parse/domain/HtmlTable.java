package com.ruoyi.project.parse.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Data
public class HtmlTable implements Table {

    /**
     * 是否被抽取过
     */
    private boolean extracted;

    private List<Cell> thList = new ArrayList<>();
    private List<List<Cell>> tdList = new ArrayList<>();

    @Override
    public List<? extends Cell> getTh() {
        return thList;
    }

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.HTML;
    }

    @Override
    public boolean isExtracted() {
        return extracted;
    }

    @Override
    public List<List<Cell>> getData() {
        return tdList;
    }

    @Override
    public void setExtracted(boolean b) {
        extracted = b;
    }

    @Override
    public void passTh() {
        if(tdList.size() > 0){
            tdList.remove(0);
        }else{
            throw new RuntimeException("当前表格不可删除指定数据");
        }
    }

    @Override
    public void setNotEmptyData(List<List<Cell>> data) {
       this.tdList = data;
    }
}