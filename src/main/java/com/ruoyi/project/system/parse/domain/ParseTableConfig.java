package com.ruoyi.project.system.parse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 格配置对象 parse_table_config
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
public class ParseTableConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表格配置 */
    private Long tableConfigId;

    /** 所属资源id */
    @Excel(name = "所属资源id")
    private Long resourceId;

    /** 表格类型 */
    @Excel(name = "表格类型")
    private Long tableType;

    /** 匹配方式 */
    @Excel(name = "匹配方式")
    private Long matchMethod;

    /** 解析器类型 */
    @Excel(name = "解析器类型")
    private Long parserType;

    /** 解析条件 */
    private String conditions;

    /** 期望解析行数 */
    private Long expectationRow;

    /** 合并相同标题表格（0不处理 1处理） */
    private String isMergeSameTitle;

    /** 合并表格行 */
    private String isMergeRow;

    /** 中断表格条件，满足条件就停止匹配 */
    private String interpretConditions;

    public void setTableConfigId(Long tableConfigId) 
    {
        this.tableConfigId = tableConfigId;
    }

    public Long getTableConfigId() 
    {
        return tableConfigId;
    }

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setTableType(Long tableType) 
    {
        this.tableType = tableType;
    }

    public Long getTableType() 
    {
        return tableType;
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

    public void setConditions(String conditions) 
    {
        this.conditions = conditions;
    }

    public String getConditions() 
    {
        return conditions;
    }

    public void setExpectationRow(Long expectationRow) 
    {
        this.expectationRow = expectationRow;
    }

    public Long getExpectationRow() 
    {
        return expectationRow;
    }

    public void setIsMergeSameTitle(String isMergeSameTitle) 
    {
        this.isMergeSameTitle = isMergeSameTitle;
    }

    public String getIsMergeSameTitle() 
    {
        return isMergeSameTitle;
    }

    public void setIsMergeRow(String isMergeRow) 
    {
        this.isMergeRow = isMergeRow;
    }

    public String getIsMergeRow() 
    {
        return isMergeRow;
    }

    public void setInterpretConditions(String interpretConditions) 
    {
        this.interpretConditions = interpretConditions;
    }

    public String getInterpretConditions() 
    {
        return interpretConditions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tableConfigId", getTableConfigId())
            .append("resourceId", getResourceId())
            .append("tableType", getTableType())
            .append("matchMethod", getMatchMethod())
            .append("parserType", getParserType())
            .append("conditions", getConditions())
            .append("expectationRow", getExpectationRow())
            .append("isMergeSameTitle", getIsMergeSameTitle())
            .append("isMergeRow", getIsMergeRow())
            .append("interpretConditions", getInterpretConditions())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
