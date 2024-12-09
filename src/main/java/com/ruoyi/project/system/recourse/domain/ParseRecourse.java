package com.ruoyi.project.system.recourse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 解析资源对象 parse_recourse
 * 
 * @author ruoyi
 * @date 2024-12-09
 */
public class ParseRecourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源id */
    private Long resourceId;

    /** 资源标题 */
    @Excel(name = "资源标题")
    private String resourceTitle;

    /** 资源类型(1:KV型, 2:列表型) */
    @Excel(name = "资源类型")
    private Long resourceType;

    /** 匹配方式(1:精确, 2:模糊) */
    @Excel(name = "匹配方式(1:精确, 2:模糊)")
    private Long matchMethod;

    /** 解析器类型(0:全部,1:spire, 2:py表格处理，3:Tabula) */
    @Excel(name = "解析器类型(0:全部,1:spire, 2:py表格处理，3:Tabula)")
    private Long parserType;

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

    public void setResourceTitle(String resourceTitle) 
    {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceTitle() 
    {
        return resourceTitle;
    }

    public void setResourceType(Long resourceType) 
    {
        this.resourceType = resourceType;
    }

    public Long getResourceType() 
    {
        return resourceType;
    }

    public void setMatchMethod(Long matchMethod) 
    {
        this.matchMethod = matchMethod;
    }

    public Long getMatchMethod() 
    {
        return matchMethod;
    }

    public void setParserType(Long parserType) 
    {
        this.parserType = parserType;
    }

    public Long getParserType() 
    {
        return parserType;
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
            .append("resourceTitle", getResourceTitle())
            .append("resourceType", getResourceType())
            .append("matchMethod", getMatchMethod())
            .append("parserType", getParserType())
            .append("location", getLocation())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
