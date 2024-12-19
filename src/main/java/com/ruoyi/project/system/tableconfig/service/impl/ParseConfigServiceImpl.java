package com.ruoyi.project.system.tableconfig.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
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

    private static final Pattern ENTRY_PATTERN = Pattern.compile("\"([^\"]+)\":\"([^\"]+)\"");
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
        parseResults.stream().map(ParseResult::getResult).forEach(resultResultStr -> parseJson(resultResultStr, resultList));
        return resultList;
    }


    public static void parseJson(String jsonString, List<List<String>> resultList) {
        // 尝试将字符串解析为JSONObject
        JSONObject jsonObject = JSON.parseObject(jsonString);

        if (jsonObject.containsKey("tableData")) {
            // 如果存在 "tableData" 键，则认为是表格数据
            JSONArray tableDataArray = jsonObject.getJSONArray("tableData");
            if (tableDataArray != null && !tableDataArray.isEmpty()) {
                parseTableData(tableDataArray, resultList);
            }
        } else if (jsonObject.containsKey("keyValuePairs")) {
            // 如果存在 "keyValuePairs" 键，则认为是键值对数据
            handleJsonObjectValues(jsonString, resultList);
        } else {
            // 如果不符合上述两种情况，则视为普通字符串
            resultList.add(Collections.singletonList(jsonString));
        }
    }

    private static void parseTableData(JSONArray tableDataArray, List<List<String>> resultList) {
        boolean isFirstRow = true;
        for (int i = 0; i < tableDataArray.size(); i++) {
            JSONArray row = tableDataArray.getJSONArray(i);
            if (isFirstRow) {
                isFirstRow = false;
                continue; // 跳过标题行
            }
            List<String> rowData = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                rowData.add(row.getString(j));
            }
            resultList.add(rowData);
        }
    }

    private static void handleJsonObjectValues(String jsonString, List<List<String>> resultList) {
        // 使用正则表达式匹配所有的键值对，并保持它们的顺序
        Matcher matcher = ENTRY_PATTERN.matcher(jsonString);
        LinkedHashMap<String, String> orderedMap = new LinkedHashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            orderedMap.put(key, value);
        }

        // 将 LinkedHashMap 的值作为一行添加到 resultList 中
        resultList.add(new ArrayList<>(orderedMap.values()));
    }
}
