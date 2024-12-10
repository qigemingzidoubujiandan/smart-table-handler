package com.ruoyi.project.system.parse.domain;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 表格配置表(parse_config)实体类
 *
 * @author zcl
 * @since 2024-12-10 09:48:07
 * @description 
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("parse_config")
public class ParseConfig extends Model<ParseConfig> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表格配置
     */
    @TableId
	private Long parseConfigId;
    private String parseConfigDesc;
    /**
     * 所属资源id
     */
    private Long resourceId;
    /**
     * 解析类型(1:表格, 2:text)
     */
    private Integer configType;
    /**
     * 表格类型(1:KV型, 2:列表型)
     */
    private Integer tableType;
    /**
     * 匹配方式(1:精确, 2:模糊)
     */
    private Integer tableMatchMethod;
    /**
     * 解析器类型(0:全部,1:spire, 2:py表格处理，3:Tabula)
     */
    private Integer tableParserType;
    /**
     * 解析条件
     */
    private String tableConditions;
    /**
     * 期望解析行数
     */
    private Long tableExpectationRow;
    /**
     * 合并相同标题表格（0不处理 1处理）
     */
    private String tableIsMergeSameTitle;
    /**
     * 合并表格行（因分页导致切断表格）【0不合并 1合并】
     */
    private String tableIsMergeRow;
    /**
     * 中断表格条件，满足条件就停止匹配
     */
    private String tableInterpretConditions;
    /**
     * 文本-正则表达式
     */
    private String textRegExpression;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 备注
     */
    private String remark;

}