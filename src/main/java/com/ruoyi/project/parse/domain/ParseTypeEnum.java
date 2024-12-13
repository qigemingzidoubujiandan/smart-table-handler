package com.ruoyi.project.parse.domain;

public enum ParseTypeEnum {
    TABLE(1, "table"),
    TEXT(2, "text");

    private final Integer code;
    private final String desc;

    ParseTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ParseTypeEnum get(Integer code) {
        for (ParseTypeEnum item : ParseTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ParseTypeEnum getByDesc(String desc) {
        for (ParseTypeEnum item : ParseTypeEnum.values()) {
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
