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

    /**
     * 构造函数接收 ExtractorConfig 对象，从中获取用于文本提取的正则表达式模式。
     * @param config 包含文本提取所需的所有配置信息的对象。
     */
    public BaseTextExtractor(ExtractorConfig config) {
        this.pattern = config.getTextPattern();
    }

    @Override
    public TextExtractedResult extract(String text) {
        String extractedText = RegUtil.applyReg(text, pattern, 1);
        return new TextExtractedResult(extractedText);
    }
}
