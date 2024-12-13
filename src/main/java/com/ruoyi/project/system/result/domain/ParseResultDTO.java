package com.ruoyi.project.system.result.domain;


import lombok.Data;

/**
 * 解析结果对象 parse_result
 *
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Data
public class ParseResultDTO extends ParseResult {
    private String resourceDesc;
    private String fileName;
    private String parseDesc;
}
