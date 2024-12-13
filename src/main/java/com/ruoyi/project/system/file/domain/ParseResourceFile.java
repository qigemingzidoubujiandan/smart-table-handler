package com.ruoyi.project.system.file.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 文件资源对象 parse_resource_file
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
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
    private Long isParsed;

    public void setResourceFileId(Long resourceFileId) 
    {
        this.resourceFileId = resourceFileId;
    }

    public Long getResourceFileId() 
    {
        return resourceFileId;
    }

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setIsParsed(Long isParsed) 
    {
        this.isParsed = isParsed;
    }

    public Long getIsParsed() 
    {
        return isParsed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resourceFileId", getResourceFileId())
            .append("resourceId", getResourceId())
            .append("fileName", getFileName())
            .append("fileType", getFileType())
            .append("location", getLocation())
            .append("isParsed", getIsParsed())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
