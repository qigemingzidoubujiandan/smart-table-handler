package com.ruoyi.project.parse.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.result.TableExtractedResult;
import com.ruoyi.project.parse.extractor.result.TextExtractedResult;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenl
 */
public class ExtractorUtil {

    private static final Gson GSON = new Gson();
    private static final Pattern ENTRY_PATTERN = Pattern.compile("\"([^\"]+)\":\"([^\"]+)\"");

    public static List<ExtractedResult> parseExtractedResult(String jsonString) {
        List<ExtractedResult> results = new ArrayList<>();

        // 解析 JSON 字符串为 Map
        Map<String, Object> jsonMap = GSON.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
        }.getType());

        if (jsonMap.containsKey("tableData")) {
            Type listType = new TypeToken<List<List<String>>>() {
            }.getType();
            List<List<String>> tableData = GSON.fromJson(GSON.toJson(jsonMap.get("tableData")), listType);
            results.add(new TableExtractedResult(tableData));

        } else if (jsonMap.containsKey("keyValuePairs")) {
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> keyValuePairs = GSON.fromJson(GSON.toJson(jsonMap.get("keyValuePairs")), mapType);
            results.add(new KVExtractedResult(keyValuePairs));

        } else if (jsonMap.containsKey("text")) {
            results.add(new TextExtractedResult(String.valueOf(jsonMap.get("text"))));
        }
        return results;
    }


    public static List<List<String>> parseJson(String jsonString) {
        List<List<String>> resultList = new ArrayList<>();

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
        return resultList;
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
