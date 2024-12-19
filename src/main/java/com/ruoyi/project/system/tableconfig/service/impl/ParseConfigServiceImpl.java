package com.ruoyi.project.system.tableconfig.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.ListUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parse.util.ExtractorUtil;
import com.ruoyi.project.system.result.domain.ParseResult;
import com.ruoyi.project.system.result.mapper.ParseResultMapper;
import com.ruoyi.project.system.tableconfig.domain.ParseConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ParseConfigServiceImpl implements IParseConfigService {
    private static final Logger log = LoggerFactory.getLogger(ParseConfigServiceImpl.class);
    @Autowired
    private ParseConfigMapper parseConfigMapper;

    @Autowired
    private ParseResultMapper parseResultMapper;


    /**
     * 查询解析配置
     *
     * @param parseConfigId 解析配置主键
     * @return 解析配置
     */
    @Override
    public ParseConfig selectParseConfigByParseConfigId(Long parseConfigId) {
        return parseConfigMapper.selectParseConfigByParseConfigId(parseConfigId);
    }

    /**
     * 查询解析配置列表
     *
     * @param parseConfig 解析配置
     * @return 解析配置
     */
    @Override
    public List<ParseConfigDTO> selectParseConfigList(ParseConfig parseConfig) {
        return parseConfigMapper.selectParseConfigList(parseConfig);
    }

    @Override
    public List<ParseConfig> selectList(ParseConfig parseConfig) {
        return parseConfigMapper.selectList(parseConfig);
    }

    /**
     * 新增解析配置
     *
     * @param parseConfig 解析配置
     * @return 结果
     */
    @Override
    public int insertParseConfig(ParseConfig parseConfig) {
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
    public int updateParseConfig(ParseConfig parseConfig) {
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
    public int deleteParseConfigByParseConfigIds(String parseConfigIds) {
        return parseConfigMapper.deleteParseConfigByParseConfigIds(Convert.toStrArray(parseConfigIds));
    }

    /**
     * 删除解析配置信息
     *
     * @param parseConfigId 解析配置主键
     * @return 结果
     */
    @Override
    public int deleteParseConfigByParseConfigId(Long parseConfigId) {
        return parseConfigMapper.deleteParseConfigByParseConfigId(parseConfigId);
    }

    @Override
    public List<String> getTitles(Long parseConfigId) {
        ParseConfig parseConfig = selectParseConfigByParseConfigId(parseConfigId);
        return ListUtil.toList(parseConfig.getConditionArr());
    }

    @Override
    public List<List<String>> getContent(Long parseConfigId) {
        ParseResult parseResult = new ParseResult();
        parseResult.setParseConfigId(parseConfigId);
        List<ParseResult> parseResults = parseResultMapper.selectList(parseResult);
        return parseResults.stream()
                .map(ParseResult::getResult)
                .map(ExtractorUtil::parseJson)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


}
