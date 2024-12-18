package com.ruoyi.project.parse.domain;

public enum TableTypeEnum {
    KV(1, "KV类型"),
    LIST(2, "列表");

    private final Integer code;
    private final String desc;

    TableTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TableTypeEnum get(Integer code) {
        for (TableTypeEnum item : TableTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static TableTypeEnum getByDesc(String desc) {
        for (TableTypeEnum item : TableTypeEnum.values()) {
            if (item.getDesc().equals(desc)) {
                return item;
            }
        }
        return null;
    }

    public static TableTypeEnum fromCode(int code) {
        for (TableTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
