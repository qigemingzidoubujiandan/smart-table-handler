package com.ruoyi.project.parse.extractor.result;

public class TextExtractedResult extends ExtractedResult {
    private final String text;

    public TextExtractedResult(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    @Override
    public String toString() {
        return "TextExtractedResult{" + "text='" + text + '\'' + '}';
    }
}