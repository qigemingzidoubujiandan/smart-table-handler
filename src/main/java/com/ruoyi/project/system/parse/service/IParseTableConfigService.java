package com.ruoyi.project.system.parse.service;

import java.util.List;
import com.ruoyi.project.system.parse.domain.ParseTableConfig;

/**
 * 格配置Service接口
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
public interface IParseTableConfigService 
{
    /**
     * 查询格配置
     * 
     * @param tableConfigId 格配置主键
     * @return 格配置
     */
    public ParseTableConfig selectParseTableConfigByTableConfigId(Long tableConfigId);

    /**
     * 查询格配置列表
     * 
     * @param parseTableConfig 格配置
     * @return 格配置集合
     */
    public List<ParseTableConfig> selectParseTableConfigList(ParseTableConfig parseTableConfig);

    /**
     * 新增格配置
     * 
     * @param parseTableConfig 格配置
     * @return 结果
     */
    public int insertParseTableConfig(ParseTableConfig parseTableConfig);

    /**
     * 修改格配置
     * 
     * @param parseTableConfig 格配置
     * @return 结果
     */
    public int updateParseTableConfig(ParseTableConfig parseTableConfig);

    /**
     * 批量删除格配置
     * 
     * @param tableConfigIds 需要删除的格配置主键集合
     * @return 结果
     */
    public int deleteParseTableConfigByTableConfigIds(String tableConfigIds);

    /**
     * 删除格配置信息
     * 
     * @param tableConfigId 格配置主键
     * @return 结果
     */
    public int deleteParseTableConfigByTableConfigId(Long tableConfigId);
}
