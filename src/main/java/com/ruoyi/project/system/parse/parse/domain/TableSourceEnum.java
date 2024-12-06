package com.ruoyi.project.system.parse.parse.domain;

public enum TableSourceEnum {
    HTML(1, "html"),
    PDF(2, "pdf"),
    WORD(3, "word");

    private final Integer code;
    private final String desc;

    TableSourceEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TableSourceEnum get(Integer code) {
        for (TableSourceEnum item : TableSourceEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static TableSourceEnum getByDesc(String desc) {
        for (TableSourceEnum item : TableSourceEnum.values()) {
            if (item.getDesc().equals(desc)) {
                return item;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
