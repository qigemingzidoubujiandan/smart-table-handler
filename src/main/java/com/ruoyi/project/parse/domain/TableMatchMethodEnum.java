package com.ruoyi.project.parse.domain;

public enum TableMatchMethodEnum {
    KV(1, "KV类型"),
    LIST(2, "列表");

    private final Integer code;
    private final String desc;

    TableMatchMethodEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TableMatchMethodEnum get(Integer code) {
        for (TableMatchMethodEnum item : TableMatchMethodEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static TableMatchMethodEnum getByDesc(String desc) {
        for (TableMatchMethodEnum item : TableMatchMethodEnum.values()) {
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
