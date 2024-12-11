package com.ruoyi.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 解析状态
 */
@Getter
@AllArgsConstructor
public enum ParseStatusEnum {
    NO("0", "未解析"),
    PARSING("2", "解析中"),
    YES("1", "已解析");

    private final String status;
    private final String desc;

    public static ParseStatusEnum getByStatus(String status) {
        return Stream.of(ParseStatusEnum.values()).filter(parseStatusEnum -> parseStatusEnum.getStatus().equals(status))
                .findFirst()
                .orElse(null);
    }

    public static ParseStatusEnum getByDesc(String desc) {
        return Stream.of(ParseStatusEnum.values()).filter(parseStatusEnum -> parseStatusEnum.getDesc().equals(desc))
                .findFirst()
                .orElse(null);
    }
}
