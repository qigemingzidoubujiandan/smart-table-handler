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
 * 资源子表(parse_recourse_file)实体类
 *
 * @author zcl
 * @since 2024-12-10 09:48:07
 * @description 
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("parse_recourse_file")
public class ParseRecourseFile extends Model<ParseRecourseFile> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源文件id
     */
    @TableId
	private Long recourseFileId;
    /**
     * 解析结果id
     */
    private Long resourceId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 资源位置
     */
    private String location;
    /**
     * 是否解析(1：是，0：否)
     */
    private Integer isParsed;
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