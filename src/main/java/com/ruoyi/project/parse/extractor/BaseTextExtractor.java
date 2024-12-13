package com.ruoyi.project.parse.extractor;

import com.ruoyi.project.parse.util.RegUtil;

import java.util.regex.Pattern;

/**
 * 文本正则抽取器
 * @author chenl
 */
public class BaseTextExtractor implements IExtractor<String, String> {

    private final Pattern pattern;

    public BaseTextExtractor(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String extract(String text) {
        return RegUtil.applyReg(text, pattern, 1);
    }

}
