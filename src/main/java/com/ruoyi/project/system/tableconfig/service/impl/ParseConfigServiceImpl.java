package com.ruoyi.project.system.tableconfig.service.impl;

import java.util.*;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.project.system.result.domain.ParseResult;
import com.ruoyi.project.system.result.mapper.ParseResultMapper;
import com.ruoyi.project.system.tableconfig.domain.ParseConfigDTO;
import org.apache.commons.compress.utils.Lists;
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
        List<List<String>> resultList = new ArrayList<>();
        for (ParseResult result : parseResults) {
            String resultResultStr = result.getResult();
            try {
                // 尝试将字符串解析为JSON数组
                JSONArray jsonArray = JSON.parseArray(resultResultStr);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    // 如果成功解析且数组非空，则认为是表格数据
                    List<List<String>> tableData = new ArrayList<>();
                    boolean isFirstRow = true;
                    for (Object rowObj : jsonArray) {
                        JSONArray row = (JSONArray) rowObj;
                        if (isFirstRow) {
                            isFirstRow = false;
                            continue; // 跳过标题行
                        }
                        List<String> rowData = new ArrayList<>();
                        for (Object cell : row) {
                            rowData.add(cell.toString());
                        }
                        tableData.add(rowData);
                    }
                    resultList.addAll(tableData);
                } else {
                    handleJsonObjectValues(resultResultStr, resultList);
                }
            } catch (JSONException e) {
                handleJsonObjectValues(resultResultStr, resultList);
            }
        }
        return resultList;
    }

    private void handleJsonObjectValues(String jsonString, List<List<String>> resultList) {
        try {
            JSONUtil.parse(jsonString);
            // 先解析为JSONObject，再转换为Map以保持键值对的顺序
            LinkedHashMap<String, String> map = JSONUtil.toBean(jsonString, LinkedHashMap.class);
            Collection<String> values = map.values();
            List<String> rowData = ListUtil.toList(values);
            resultList.add(rowData);
        } catch (JSONException e) {
            // 如果解析失败，则认为是普通字符串
            resultList.add(ListUtil.of(jsonString));
        }
    }
}
