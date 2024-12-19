package com.ruoyi.project.parse.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.result.TableExtractedResult;
import com.ruoyi.project.parse.extractor.result.TextExtractedResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenl
 */
public class ExtractorUtil {

    private static final Gson GSON = new Gson();

    public static List<ExtractedResult> parseJson(String jsonString) {
        List<ExtractedResult> results = new ArrayList<>();

        // 解析 JSON 字符串为 Map
        Map<String, Object> jsonMap = GSON.fromJson(jsonString, new TypeToken<Map<String, Object>>() {}.getType());

        if (jsonMap.containsKey("tableData")) {
            Type listType = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> tableData = GSON.fromJson(GSON.toJson(jsonMap.get("tableData")), listType);
            results.add(new TableExtractedResult(tableData));

        } else if (jsonMap.containsKey("keyValuePairs")) {
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> keyValuePairs = GSON.fromJson(GSON.toJson(jsonMap.get("keyValuePairs")), mapType);
            results.add(new KVExtractedResult(keyValuePairs));

        } else if (jsonMap.containsKey("text")) {
            results.add(new TextExtractedResult(String.valueOf(jsonMap.get("text"))));
        }
        return results;
    }
}
