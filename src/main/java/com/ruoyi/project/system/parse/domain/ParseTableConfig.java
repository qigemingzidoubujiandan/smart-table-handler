package com.ruoyi.project.system.parse.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 格配置对象 parse_table_config
 *
 * @author zhaochenliang
 * @date 2024-12-06
 */
@Data
@TableName("parse_table_config")
public class ParseTableConfig extends Model<ParseTableConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 表格配置
     */
    private Long tableConfigId;

    /**
     * 配置描述
     */
    private String configDesc;

    /**
     * 所属资源id
     */
    @Excel(name = "所属资源id")
    private Long resourceId;

    /**
     * 表格类型
     */
    @Excel(name = "表格类型")
    private Long tableType;

    /**
     * 匹配方式
     */
    @Excel(name = "匹配方式")
    private Long matchMethod;

    /**
     * 解析器类型
     */
    @Excel(name = "解析器类型")
    private Long parserType;

    /**
     * 解析条件
     */
    private String conditions;

    /**
     * 期望解析行数
     */
    private Long expectationRow;

    /**
     * 合并相同标题表格（0不处理 1处理）
     */
    private String isMergeSameTitle;

    /**
     * 合并表格行
     */
    private String isMergeRow;

    /**
     * 中断表格条件，满足条件就停止匹配
     */
    private String interpretConditions;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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
