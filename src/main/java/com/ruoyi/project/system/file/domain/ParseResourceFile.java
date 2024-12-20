package com.ruoyi.project.system.file.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * 文件资源对象 parse_resource_file
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Data
public class ParseResourceFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源文件id */
    private Long resourceFileId;

    /** 资源id */
    @Excel(name = "资源id")
    private Long resourceId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 资源位置 */
    @Excel(name = "资源位置")
    private String location;

    /** 是否解析 */
    @Excel(name = "是否解析")
    private Integer isParsed;

}
