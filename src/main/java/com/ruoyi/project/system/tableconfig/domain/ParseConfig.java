package com.ruoyi.project.system.tableconfig.domain;

import com.ruoyi.common.utils.text.Convert;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 解析配置对象 parse_config
 * 
 * @author zz
 * @date 2024-12-10
 */
public class ParseConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置 */
    private Long parseConfigId;

    /** 所属资源id */
    @Excel(name = "所属资源id")
    private Long resourceId;

    /** 解析条件 */
    @Excel(name = "解析条件")
    private String parseDesc;

    /** 解析类型 */
    @Excel(name = "解析类型")
    private Long configType;

    /** 表格类型 */
    @Excel(name = "表格类型")
    private Long tableType;

    /** 匹配方式 */
    @Excel(name = "匹配方式")
    private Long tableMatchMethod;

    /** 解析器类型 */
    private Long tableParserType;

    /** 解析条件 */
    private String tableConditions;

    /** 期望解析行数 */
    private Long tableExpectationRow;

    /** 合并相同标题表格 */
    private String tableIsMergeSameTitle;

    /** 合并表格行 */
    private String tableIsMergeRow;

    /** 中断表格 */
    private String tableInterpretConditions;

    /** 文本:正则表达式 */
    private String textRegExpression;

    public void setParseConfigId(Long parseConfigId) 
    {
        this.parseConfigId = parseConfigId;
    }

    public Long getParseConfigId() 
    {
        return parseConfigId;
    }

    public void setResourceId(Long resourceId) 
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId() 
    {
        return resourceId;
    }

    public void setParseDesc(String parseDesc) 
    {
        this.parseDesc = parseDesc;
    }

    public String getParseDesc() 
    {
        return parseDesc;
    }

    public void setConfigType(Long configType) 
    {
        this.configType = configType;
    }

    public Long getConfigType() 
    {
        return configType;
    }

    public void setTableType(Long tableType) 
    {
        this.tableType = tableType;
    }

    public Long getTableType() 
    {
        return tableType;
    }

    public void setTableMatchMethod(Long tableMatchMethod) 
    {
        this.tableMatchMethod = tableMatchMethod;
    }

    public Long getTableMatchMethod() 
    {
        return tableMatchMethod;
    }

    public void setTableParserType(Long tableParserType) 
    {
        this.tableParserType = tableParserType;
    }

    public Long getTableParserType() 
    {
        return tableParserType;
    }

    public void setTableConditions(String tableConditions) 
    {
        this.tableConditions = tableConditions;
    }

    public String getTableConditions() 
    {
        return tableConditions;
    }

    public String[] getConditionArr() {
        return Convert.toStrArray(tableConditions);
    }

    public void setTableExpectationRow(Long tableExpectationRow) 
    {
        this.tableExpectationRow = tableExpectationRow;
    }

    public Long getTableExpectationRow() 
    {
        return tableExpectationRow;
    }

    public void setTableIsMergeSameTitle(String tableIsMergeSameTitle) 
    {
        this.tableIsMergeSameTitle = tableIsMergeSameTitle;
    }

    public String getTableIsMergeSameTitle() 
    {
        return tableIsMergeSameTitle;
    }

    public void setTableIsMergeRow(String tableIsMergeRow) 
    {
        this.tableIsMergeRow = tableIsMergeRow;
    }

    public String getTableIsMergeRow() 
    {
        return tableIsMergeRow;
    }

    public void setTableInterpretConditions(String tableInterpretConditions) 
    {
        this.tableInterpretConditions = tableInterpretConditions;
    }

    public String getTableInterpretConditions() 
    {
        return tableInterpretConditions;
    }

    public void setTextRegExpression(String textRegExpression) 
    {
        this.textRegExpression = textRegExpression;
    }

    public String getTextRegExpression() 
    {
        return textRegExpression;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("parseConfigId", getParseConfigId())
            .append("resourceId", getResourceId())
            .append("parseDesc", getParseDesc())
            .append("configType", getConfigType())
            .append("tableType", getTableType())
            .append("tableMatchMethod", getTableMatchMethod())
            .append("tableParserType", getTableParserType())
            .append("tableConditions", getTableConditions())
            .append("tableExpectationRow", getTableExpectationRow())
            .append("tableIsMergeSameTitle", getTableIsMergeSameTitle())
            .append("tableIsMergeRow", getTableIsMergeRow())
            .append("tableInterpretConditions", getTableInterpretConditions())
            .append("textRegExpression", getTextRegExpression())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
