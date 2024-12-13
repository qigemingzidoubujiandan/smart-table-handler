package com.ruoyi.project.parse.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * pdf 表格行 元素
 */
@AllArgsConstructor
@NoArgsConstructor
public class PDFTableCell implements Cell<String, String> {

    private String text;

    private String unit;

    public PDFTableCell(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public void setText(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return "PDFTableCell{" +
                "text='" + text + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }

    @Override
    public String ext() {
        return unit;
    }

    @Override
    public void setExt(String ext) {
        this.unit = ext;
    }
}
