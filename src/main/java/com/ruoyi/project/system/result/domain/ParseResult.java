package com.ruoyi.project.system.result.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 解析结果对象 parse_result
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
public class ParseResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 解析结果id */
    private Long parseResultId;

    /** 资源id */
    @Excel(name = "资源id")
    private Long resourceId;

    /** 资源文件id */
    @Excel(name = "资源文件id")
    private Long recourseFileId;

    /** 配置id */
    @Excel(name = "配置id")
    private Long parseConfigId;

    /** 解析结果 */
    @Excel(name = "解析结果")
    private String result;

    public void setParseResultId(Long parseResultId) 
    {
        this.parseResultId = parseResultId;
    }

    public Long getParseResultId() 
    {
        return parseResultId;
    }

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setRecourseFileId(Long recourseFileId) 
    {
        this.recourseFileId = recourseFileId;
    }

    public Long getRecourseFileId() 
    {
        return recourseFileId;
    }

    public void setParseConfigId(Long parseConfigId) 
    {
        this.parseConfigId = parseConfigId;
    }

    public Long getParseConfigId() 
    {
        return parseConfigId;
    }

    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("parseResultId", getParseResultId())
            .append("resourceId", getResourceId())
            .append("recourseFileId", getRecourseFileId())
            .append("parseConfigId", getParseConfigId())
            .append("result", getResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
