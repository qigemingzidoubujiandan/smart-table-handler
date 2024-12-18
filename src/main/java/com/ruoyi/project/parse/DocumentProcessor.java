package com.ruoyi.project.parse;

import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.extractor.ExtractorConfig;
import com.ruoyi.project.parse.extractor.ExtractorConvertor;
import com.ruoyi.project.parse.extractor.IExtractor;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.result.TextExtractedResult;
import com.ruoyi.project.parse.filefetcher.FileFetcher;
import com.ruoyi.project.parse.parser.IParser;
import com.ruoyi.project.parse.parser.ParserFactory;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

/**
 * @author chenl
 */
import java.util.List;
import java.util.Optional;

public class DocumentProcessor {

    public static List<ExtractedResult> processFile(FileFetcher fileFetcher, ExtractorConfig config) {
        try {
            // 0. 获取文件路径
            String sourcePath = fileFetcher.fetchFile();

            // 1. 根据配置获取合适的解析器并解析文件
            IParser<String, ?> parser = ParserFactory.createParserByFilePath(sourcePath, config.getParseType());
            Object parsed = parser.parse(sourcePath);

            // 2. 根据配置获取合适的抽取器并抽取信息
            IExtractor<Object, ? extends ExtractedResult> extractor = ExtractorConvertor.createExtractor(config);

            // 3. 处理抽取结果
            ExtractedResult result = extractor.extract(parsed);

            // 4. 返回结果列表（这里假设我们总是得到一个非空的结果）
            return Optional.ofNullable(result)
                    .map(List::of)
                    .orElseGet(List::of);

        } catch (Exception e) {
            // 错误处理逻辑
            // 日志记录 todo:
            throw new RuntimeException("Failed to process file", e);
        }
    }
}