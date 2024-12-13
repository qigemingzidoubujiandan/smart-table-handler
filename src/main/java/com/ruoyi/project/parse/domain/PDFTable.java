package com.ruoyi.project.parse.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * pdf 表格的属性
 */
@SuppressWarnings("rawtypes")
@Data
public class PDFTable implements Table {

    /**
     * 是否被抽取过
     */
    private boolean extracted;

    private List<List<PDFTableCell>> data;

    /**
     * 割裂的表格没法区分表头和数据
     */
    @Override
    public List<List<Cell>> getData() {
        return data.stream().map(r -> r.stream().map(Cell.class::cast).collect(Collectors.toList()))
                .collect(Collectors.toList());
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
        List<List<PDFTableCell>> pdfData = new ArrayList<>();
        data.forEach(th -> {
            List<PDFTableCell> pdfTh = new ArrayList<>();
            th.forEach(cell -> {
                pdfTh.add((PDFTableCell) cell);
            });
            pdfData.add(pdfTh);
        });
        this.data = pdfData;
    }

    @Override
    public List<PDFTableCell> getTh() {
        // passTh()后，data可能为空
        return data.size() > 0 ? data.get(0) : null;
    }

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.PDF;
    }

    @Override
    public String toString() {
        return "PDFTable{extracted=" + extracted +
                ", data=" + data +
                '}';
    }
}
