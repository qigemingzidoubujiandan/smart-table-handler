package com.ruoyi.project.system.parse.parse.domain;

public enum FileTypeEnum {
    HTML(1, "html"),
    PDF(2, "pdf"),
    WORD(3, "word");

    private final Integer code;
    private final String desc;

    FileTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FileTypeEnum get(Integer code) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static FileTypeEnum getByDesc(String desc) {
        for (FileTypeEnum item : FileTypeEnum.values()) {
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
