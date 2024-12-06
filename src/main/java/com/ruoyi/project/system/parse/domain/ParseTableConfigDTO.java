package com.ruoyi.project.system.parse.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.system.parse.parse.extractor.AbstractTableExtractor;
import com.ruoyi.project.system.parse.parse.extractor.TableExtractAdoptor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 格配置对象 parse_table_config
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
public class ParseTableConfigDTO extends AbstractTableExtractor
{

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


    @Override
    public int expectParseRowSize() {
        return expectationRow.intValue();
    }

    @Override
    public String[] condition() {
        return conditions.split(",");
    }

    @Override
    public boolean parsed() {
        return false;
    }

    @Override
    public void fillMatchedData(Object o) {

    }

    @Override
    public Object matchedData() {
        return null;
    }
}
