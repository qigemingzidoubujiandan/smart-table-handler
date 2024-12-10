package com.ruoyi.project.system.tableconfig.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.tableconfig.mapper.ParseConfigMapper;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.service.IParseConfigService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 解析配置Service业务层处理
 * 
 * @author zz
 * @date 2024-12-10
 */
@Service
public class ParseConfigServiceImpl implements IParseConfigService 
{
    @Autowired
    private ParseConfigMapper parseConfigMapper;

    /**
     * 查询解析配置
     * 
     * @param parseConfigId 解析配置主键
     * @return 解析配置
     */
    @Override
    public ParseConfig selectParseConfigByParseConfigId(Long parseConfigId)
    {
        return parseConfigMapper.selectParseConfigByParseConfigId(parseConfigId);
    }

    /**
     * 查询解析配置列表
     * 
     * @param parseConfig 解析配置
     * @return 解析配置
     */
    @Override
    public List<ParseConfig> selectParseConfigList(ParseConfig parseConfig)
    {
        return parseConfigMapper.selectParseConfigList(parseConfig);
    }

    /**
     * 新增解析配置
     * 
     * @param parseConfig 解析配置
     * @return 结果
     */
    @Override
    public int insertParseConfig(ParseConfig parseConfig)
    {
        parseConfig.setCreateTime(DateUtils.getNowDate());
        return parseConfigMapper.insertParseConfig(parseConfig);
    }

    /**
     * 修改解析配置
     * 
     * @param parseConfig 解析配置
     * @return 结果
     */
    @Override
    public int updateParseConfig(ParseConfig parseConfig)
    {
        parseConfig.setUpdateTime(DateUtils.getNowDate());
        return parseConfigMapper.updateParseConfig(parseConfig);
    }

    /**
     * 批量删除解析配置
     * 
     * @param parseConfigIds 需要删除的解析配置主键
     * @return 结果
     */
    @Override
    public int deleteParseConfigByParseConfigIds(String parseConfigIds)
    {
        return parseConfigMapper.deleteParseConfigByParseConfigIds(Convert.toStrArray(parseConfigIds));
    }

    /**
     * 删除解析配置信息
     * 
     * @param parseConfigId 解析配置主键
     * @return 结果
     */
    @Override
    public int deleteParseConfigByParseConfigId(Long parseConfigId)
    {
        return parseConfigMapper.deleteParseConfigByParseConfigId(parseConfigId);
    }
}
