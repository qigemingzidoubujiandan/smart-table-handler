package com.ruoyi.project.system.tableconfig.domain;

import com.ruoyi.common.utils.text.Convert;
import lombok.Data;
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
@Data
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
    private Integer configType;

    /** 表格类型 */
    @Excel(name = "表格类型")
    private Integer tableType;

    /** 匹配方式 */
    @Excel(name = "匹配方式")
    private Integer tableMatchMethod;

    /** 解析器类型 */
    private Integer tableParserType;

    /** 解析条件 */
    private String tableConditions;

    /** 期望解析行数 */
    private Integer tableExpectationRow;

    /** 合并相同标题表格 */
    private Integer tableIsMergeSameTitle;

    /** 合并表格行 */
    private Integer tableIsMergeRow;

    /** 移除空行 */
    private Integer tableDelEmptyRow;

    /** 中断表格 */
    private String tableInterpretConditions;

    /** 文本:正则表达式 */
    private String textRegExpression;

    public String[] getConditionArr() {
        return Convert.toStrArray(tableConditions);
    }
}
