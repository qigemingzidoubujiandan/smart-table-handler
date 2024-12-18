package com.ruoyi.project.parse.extractor.result;


import java.util.Map;

public class KVExtractedResult extends ExtractedResult {
    private final Map<String, String> keyValuePairs;

    public KVExtractedResult(Map<String, String> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }

    public Map<String, String> getKeyValuePairs() {
        return keyValuePairs;
    }

    @Override
    public String getType() {
        return "KEY_VALUE";
    }

    @Override
    public String toString() {
        return "KVExtractedResult{" + "keyValuePairs=" + keyValuePairs + '}';
    }
}
