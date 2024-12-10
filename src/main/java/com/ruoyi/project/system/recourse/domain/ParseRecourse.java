package com.ruoyi.project.system.recourse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 资源对象 parse_recourse
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public class ParseRecourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源id */
    private Long resourceId;

    /** 资源描述 */
    @Excel(name = "资源描述")
    private String resourceDesc;

    /** 是否解析 */
    @Excel(name = "是否解析")
    private Long isParsed;

    /** 资源位置 */
    @Excel(name = "资源位置")
    private String location;

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setResourceDesc(String resourceDesc) 
    {
        this.resourceDesc = resourceDesc;
    }

    public String getResourceDesc() 
    {
        return resourceDesc;
    }

    public void setIsParsed(Long isParsed) 
    {
        this.isParsed = isParsed;
    }

    public Long getIsParsed() 
    {
        return isParsed;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resourceId", getResourceId())
            .append("resourceDesc", getResourceDesc())
            .append("isParsed", getIsParsed())
            .append("location", getLocation())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
