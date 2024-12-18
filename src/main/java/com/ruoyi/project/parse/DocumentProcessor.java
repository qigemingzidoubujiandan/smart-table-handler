package com.ruoyi.project.parse;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.project.parse.extractor.ExtractorConfig;
import com.ruoyi.project.parse.extractor.ExtractorConvertor;
import com.ruoyi.project.parse.extractor.IExtractor;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.filefetcher.FileFetcher;
import com.ruoyi.project.parse.parser.IParser;
import com.ruoyi.project.parse.parser.ParserFactory;

import java.util.List;


import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author chenl
 */
@Slf4j
public class DocumentProcessor {


    private final ExecutorService executorService;

    public DocumentProcessor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * 处理文件列表并提取信息。
     *
     * @param fileFetcher 文件获取器
     * @param config      提取配置
     * @return 提取结果列表
     */
    public List<ExtractedResult> processFiles(FileFetcher fileFetcher, ExtractorConfig config) {
        try {
            // 0. 获取文件路径列表
            List<String> sourcePaths = fileFetcher.fetchFiles();
            log.info("【DocumentProcessor】 Processing {} files.", sourcePaths.size());

            // 1. 根据配置获取合适的解析器和抽取器
            IExtractor<Object, ? extends ExtractedResult> extractor = ExtractorConvertor.createExtractor(config);

            // 2. 创建解析器（这里假设所有文件类型相同，否则需要根据文件类型分别创建）
            IParser<String, ?> parser = ParserFactory.createParserByFilePath(sourcePaths.get(0), config.getParseType());

            // 3. 使用 CompletableFuture 并发处理文件
            List<CompletableFuture<? extends ExtractedResult>> futures = sourcePaths.stream()
                    .map(path -> CompletableFuture.supplyAsync(
                            () -> processSingleFile(path, parser, extractor),
                            executorService
                    ).exceptionally(ex -> {
                        log.error("【DocumentProcessor】 Failed to process file at path: {}", path, ex);
                        return null; // 返回空的结果以继续处理其他文件
                    }))
                    .collect(Collectors.toList());

            // 4. 等待所有 CompletableFuture 完成并收集结果
            List<ExtractedResult> results = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("【DocumentProcessor】 Finished processing all files.");

            return results;

        } catch (Exception e) {
            log.error("【DocumentProcessor】 Failed to process files", e);
            throw new ServiceException("【DocumentProcessor】 Failed to process files");
        } finally {
            // 5. 关闭线程池
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    /**
     * 处理单个文件并提取信息。
     *
     * @param sourcePath 文件路径
     * @param parser     解析器
     * @param extractor  抽取器
     * @return 提取结果
     */
    private ExtractedResult processSingleFile(String sourcePath, IParser<String, ?> parser, IExtractor<Object, ? extends ExtractedResult> extractor) {
        try {
            log.debug("【DocumentProcessor】 Processing file at path: {}", sourcePath);

            // 解析文件
            Object parsed = parser.parse(sourcePath);
            log.debug("【DocumentProcessor】 Parsed content from file: {}", sourcePath);

            // 抽取信息
            ExtractedResult result = extractor.extract(parsed);
            log.debug("【DocumentProcessor】 Extracted result for file: {}", sourcePath);

            return result;

        } catch (Exception e) {
            log.error("【DocumentProcessor】 Failed to process file at path: {}", sourcePath, e);
            return null; // 返回 null 以继续处理其他文件
        }
    }

}