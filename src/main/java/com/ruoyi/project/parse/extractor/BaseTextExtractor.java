package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.extractor.result.TextExtractedResult;
import com.ruoyi.project.parse.util.RegUtil;

import java.util.regex.Pattern;

/**
 * 文本正则抽取器
 * @author chenl
 */
public class BaseTextExtractor implements IExtractor<String, TextExtractedResult> {

    private final Pattern pattern;

    public BaseTextExtractor(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public TextExtractedResult extract(String text) {
        String extractedText = RegUtil.applyReg(text, pattern, 1);
        return new TextExtractedResult(extractedText);
    }
}
