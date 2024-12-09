package com.ruoyi.project.system.parse.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.system.parse.domain.ParseTableConfig;

/**
 * 表格配置Mapper接口
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
public interface ParseTableConfigMapper  extends BaseMapper<ParseTableConfig>
{
    /**
     * 查询表格配置
     * 
     * @param tableConfigId 表格配置主键
     * @return 表格配置
     */
    public ParseTableConfig selectParseTableConfigByTableConfigId(Long tableConfigId);

    /**
     * 查询表格配置列表
     * 
     * @param parseTableConfig 表格配置
     * @return 表格配置集合
     */
    public List<ParseTableConfig> selectParseTableConfigList(ParseTableConfig parseTableConfig);

    /**
     * 新增表格配置
     * 
     * @param parseTableConfig 表格配置
     * @return 结果
     */
    public int insertParseTableConfig(ParseTableConfig parseTableConfig);

    /**
     * 修改表格配置
     * 
     * @param parseTableConfig 表格配置
     * @return 结果
     */
    public int updateParseTableConfig(ParseTableConfig parseTableConfig);

    /**
     * 删除表格配置
     * 
     * @param tableConfigId 表格配置主键
     * @return 结果
     */
    public int deleteParseTableConfigByTableConfigId(Long tableConfigId);

    /**
     * 批量删除表格配置
     * 
     * @param tableConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParseTableConfigByTableConfigIds(String[] tableConfigIds);
}
