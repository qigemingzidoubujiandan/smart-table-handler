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
 * 资源表(parse_recourse)实体类
 *
 * @author zcl
 * @since 2024-12-10 09:48:07
 * @description 
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("parse_recourse")
public class ParseRecourse extends Model<ParseRecourse> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @TableId
	private Long resourceId;
    /**
     * 资源描述
     */
    private String resourceDesc;
    /**
     * 是否解析(1：是，0：否)
     */
    private Integer isParsed;
    /**
     * 资源位置
     */
    private String location;
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