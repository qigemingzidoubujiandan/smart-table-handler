package com.ruoyi.project.parse.domain;

import com.ruoyi.project.parse.domain.Enum.FileTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * pdf 表格的属性
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("rawtypes")
@Data
public class PDFTable extends Table {

    @Override
    public void setData(List<List<Cell>> data) {
        List<List<Cell>> pdfData = new ArrayList<>();
        data.forEach(th -> {
            List<Cell> pdfTh = new ArrayList<>();
            th.forEach(cell -> {
                pdfTh.add(cell);
            });
            pdfData.add(pdfTh);
        });
        this.setData(pdfData);
    }

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.PDF;
    }

}
