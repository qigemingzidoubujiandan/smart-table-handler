package com.ruoyi.project.system.tableconfig.mapper;

import java.util.List;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.domain.ParseConfigDTO;

/**
 * 解析配置Mapper接口
 * 
 * @author zz
 * @date 2024-12-10
 */
public interface ParseConfigMapper 
{
    /**
     * 查询解析配置
     * 
     * @param parseConfigId 解析配置主键
     * @return 解析配置
     */
    public ParseConfig selectParseConfigByParseConfigId(Long parseConfigId);

    /**
     * 查询解析配置列表
     * 
     * @param parseConfig 解析配置
     * @return 解析配置集合
     */
    public List<ParseConfigDTO> selectParseConfigList(ParseConfig parseConfig);

    public List<ParseConfig> selectList(ParseConfig parseConfig);


    /**
     * 新增解析配置
     * 
     * @param parseConfig 解析配置
     * @return 结果
     */
    public int insertParseConfig(ParseConfig parseConfig);

    /**
     * 修改解析配置
     * 
     * @param parseConfig 解析配置
     * @return 结果
     */
    public int updateParseConfig(ParseConfig parseConfig);

    /**
     * 删除解析配置
     * 
     * @param parseConfigId 解析配置主键
     * @return 结果
     */
    public int deleteParseConfigByParseConfigId(Long parseConfigId);

    /**
     * 批量删除解析配置
     * 
     * @param parseConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParseConfigByParseConfigIds(String[] parseConfigIds);
}
