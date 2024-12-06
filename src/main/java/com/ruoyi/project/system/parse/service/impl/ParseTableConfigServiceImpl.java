package com.ruoyi.project.system.parse.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.parse.mapper.ParseTableConfigMapper;
import com.ruoyi.project.system.parse.domain.ParseTableConfig;
import com.ruoyi.project.system.parse.service.IParseTableConfigService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 格配置Service业务层处理
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
@Service
public class ParseTableConfigServiceImpl implements IParseTableConfigService 
{
    @Autowired
    private ParseTableConfigMapper parseTableConfigMapper;

    /**
     * 查询格配置
     * 
     * @param tableConfigId 格配置主键
     * @return 格配置
     */
    @Override
    public ParseTableConfig selectParseTableConfigByTableConfigId(Long tableConfigId)
    {
        return parseTableConfigMapper.selectParseTableConfigByTableConfigId(tableConfigId);
    }

    /**
     * 查询格配置列表
     * 
     * @param parseTableConfig 格配置
     * @return 格配置
     */
    @Override
    public List<ParseTableConfig> selectParseTableConfigList(ParseTableConfig parseTableConfig)
    {
        return parseTableConfigMapper.selectParseTableConfigList(parseTableConfig);
    }

    /**
     * 新增格配置
     * 
     * @param parseTableConfig 格配置
     * @return 结果
     */
    @Override
    public int insertParseTableConfig(ParseTableConfig parseTableConfig)
    {
        parseTableConfig.setCreateTime(DateUtils.getNowDate());
        return parseTableConfigMapper.insertParseTableConfig(parseTableConfig);
    }

    /**
     * 修改格配置
     * 
     * @param parseTableConfig 格配置
     * @return 结果
     */
    @Override
    public int updateParseTableConfig(ParseTableConfig parseTableConfig)
    {
        parseTableConfig.setUpdateTime(DateUtils.getNowDate());
        return parseTableConfigMapper.updateParseTableConfig(parseTableConfig);
    }

    /**
     * 批量删除格配置
     * 
     * @param tableConfigIds 需要删除的格配置主键
     * @return 结果
     */
    @Override
    public int deleteParseTableConfigByTableConfigIds(String tableConfigIds)
    {
        return parseTableConfigMapper.deleteParseTableConfigByTableConfigIds(Convert.toStrArray(tableConfigIds));
    }

    /**
     * 删除格配置信息
     * 
     * @param tableConfigId 格配置主键
     * @return 结果
     */
    @Override
    public int deleteParseTableConfigByTableConfigId(Long tableConfigId)
    {
        return parseTableConfigMapper.deleteParseTableConfigByTableConfigId(tableConfigId);
    }
}
