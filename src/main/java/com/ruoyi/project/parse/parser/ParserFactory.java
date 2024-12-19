package com.ruoyi.project.parse.parser;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Maps;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.project.parse.domain.Enum.FileTypeEnum;
import com.ruoyi.project.parse.domain.Enum.ParseTypeEnum;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author chenl
 */
public class ParserFactory {

    // 解析器映射表，用于根据文件类型和解析类型获取对应的解析器类列表
    private static final Map<FileTypeEnum, Map<ParseTypeEnum, List<Class<? extends IParser<String, ?>>>>> PARSER_MAP = new HashMap<>();

    // 缓存实例化后的解析器对象
    private static final ConcurrentHashMap<String, IParser<String, ?>> CACHE = new ConcurrentHashMap<>();

    static {
        registerParser(FileTypeEnum.PDF, ParseTypeEnum.TABLE, SpirePDFTableParser.class);
        registerParser(FileTypeEnum.PDF, ParseTypeEnum.TEXT, PDFTextParser.class);
        registerParser(FileTypeEnum.WORD, ParseTypeEnum.TABLE, WordTableParser.class);
        registerParser(FileTypeEnum.WORD, ParseTypeEnum.TEXT, WordTxtParser.class);
        registerParser(FileTypeEnum.HTML, ParseTypeEnum.TABLE, HtmlTableParser.class);
        registerParser(FileTypeEnum.HTML, ParseTypeEnum.TEXT, HtmlTxtParser.class);

        // 注册多个解析器
        registerParser(FileTypeEnum.PDF, ParseTypeEnum.TABLE, TabulaPDFTableParser.class);
//        registerParser(FileTypeEnum.PDF, ParseTypeEnum.TABLE, PyPDFPlumberTableParser.class);
    }

    /**
     * 注册解析器到映射表中。
     */
    public static void registerParser(FileTypeEnum fileType, ParseTypeEnum parserType, Class<? extends IParser<String, ?>> parserClass) {
        PARSER_MAP.computeIfAbsent(fileType, k -> new HashMap<>())
                .computeIfAbsent(parserType, k -> new ArrayList<>())
                .add(parserClass);
    }

    /**
     * 根据文件类型和解析类型创建解析器，默认选择所有可用的解析器。
     */
    @SuppressWarnings("unchecked")
    public static IParser<String, ?> createParser(FileTypeEnum fileType, ParseTypeEnum parserType) {
        return createParser(fileType, parserType, null, false);
    }

    /**
     * 根据文件类型、解析类型和自定义解析器类创建解析器，可以选择是否使用缓存。
     */
    @SuppressWarnings("unchecked")
    public static IParser<String, ?> createParser(FileTypeEnum fileType, ParseTypeEnum parserType, Class<? extends AbstractTableParser<String>> customparserclass, boolean useCache) {
        if (customparserclass != null) {
            return instantiateParser(customparserclass, useCache);
        } else {
            String cacheKey = fileType.name() + "_" + parserType.name();
            List<Class<? extends IParser<String, ?>>> parserClasses = PARSER_MAP.getOrDefault(fileType, Maps.newHashMap())
                    .getOrDefault(parserType, Collections.emptyList());
            if (parserClasses.isEmpty()) {
                throw new IllegalArgumentException("Unsupported combination of file type and parser type");
            }
            // 如果有多个解析器，则创建 CompositeParser
            if (parserClasses.size() > 1) {
                // 强制转换为正确的泛型类型
                List<Class<? extends AbstractTableParser<String>>> stringParsers = parserClasses.stream()
                        .map(cls -> (Class<? extends AbstractTableParser<String>>) cls)
                        .collect(Collectors.toList());
                return new CompositeParser(stringParsers);
            }
            // 否则默认选择第一个解析器类
            return instantiateParser(parserClasses.get(0), useCache, cacheKey);
        }
    }

    /**
     * 实例化解析器对象。
     */
    @SuppressWarnings("unchecked")
    private static IParser<String, ?> instantiateParser(Class<? extends IParser<String, ?>> parserClass, boolean useCache, String... cacheKey) {
        try {
            if (useCache && cacheKey.length > 0) {
                return CACHE.computeIfAbsent(cacheKey[0], key -> {
                    try {
                        return parserClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new ServiceException("实例化失败");
                    }
                });
            } else {
                return parserClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new ServiceException("实例化失败");
        }
    }

    public static FileTypeEnum getFileTypeByExtension(String filePath) {
        String extension = FileUtil.getSuffix(filePath).toLowerCase();

        switch (extension) {
            case "pdf":
                return FileTypeEnum.PDF;
            case "doc":
            case "docx":
                return FileTypeEnum.WORD;
            case "html":
            case "htm":
                return FileTypeEnum.HTML;
            default:
                throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
    }

    /**
     * 根据文件路径和解析类型创建解析器。
     */
    public static IParser<String, ?> createParserByFilePath(String filePath, ParseTypeEnum parserType) {
        FileTypeEnum fileType = getFileTypeByExtension(filePath);
        return createParser(fileType, parserType);
    }

    /**
     * 根据文件路径、解析类型和自定义解析器类创建解析器。
     */
    public static IParser<String, ?> createParserByFilePath(String filePath, ParseTypeEnum parserType, Class<? extends AbstractTableParser<String>> customParserClass) {
        FileTypeEnum fileType = getFileTypeByExtension(filePath);
        return createParser(fileType, parserType, customParserClass, false);
    }

    /**
     * 获取所有注册的解析器类。
     */
    public static List<Class<? extends IParser<String, ?>>> getParsers(FileTypeEnum fileType, ParseTypeEnum parserType) {
        return PARSER_MAP.getOrDefault(fileType, Maps.newHashMap())
                .getOrDefault(parserType, Collections.emptyList());
    }
}