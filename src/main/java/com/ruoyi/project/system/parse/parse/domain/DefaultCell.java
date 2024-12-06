package com.ruoyi.project.system.parse.parse.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * cell
 */
@AllArgsConstructor
@NoArgsConstructor
public class DefaultCell implements Cell<String, String>{

    private String text;

    private String ext;

    public DefaultCell(String text) {
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
    public String ext() {
        return ext;
    }

    @Override
    public void setExt(String r) {
        ext = r;
    }

}
