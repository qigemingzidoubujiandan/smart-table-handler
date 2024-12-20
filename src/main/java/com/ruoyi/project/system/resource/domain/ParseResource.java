package com.ruoyi.project.system.resource.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * 资源对象 parse_resource
 * 
 * @date 2024-12-10
 */
@Data
public class ParseResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源id */
    private Long resourceId;

    /** 资源描述 */
    @Excel(name = "资源描述")
    private String resourceDesc;

    /** 是否解析 */
    @Excel(name = "是否解析")
    private Integer isParsed;

    /** 资源位置 */
    @Excel(name = "资源位置")
    private String location;

}
