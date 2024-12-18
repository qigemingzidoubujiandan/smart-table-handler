package com.ruoyi.project.system.resource.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.github.junrar.Junrar;
import com.google.common.collect.Lists;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.parse.DocumentProcessor;
import com.ruoyi.project.parse.domain.Table;
import com.ruoyi.project.parse.extractor.ExtractorConfig;
import com.ruoyi.project.parse.extractor.result.ExtractedResult;
import com.ruoyi.project.parse.extractor.result.KVExtractedResult;
import com.ruoyi.project.parse.extractor.result.TableExtractedResult;
import com.ruoyi.project.parse.extractor.result.TextExtractedResult;
import com.ruoyi.project.parse.filefetcher.FileFetcher;
import com.ruoyi.project.system.file.domain.ParseResourceFile;
import com.ruoyi.project.system.file.mapper.ParseResourceFileMapper;
import com.ruoyi.project.system.resource.convert.ConfigConverter;
import com.ruoyi.project.system.result.domain.ParseResult;
import com.ruoyi.project.system.result.mapper.ParseResultMapper;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.mapper.ParseConfigMapper;
import com.ruoyi.project.parse.domain.ParseTypeEnum;
import com.ruoyi.project.parse.extractor.ExtractorConvertor;
import com.ruoyi.project.parse.extractor.IExtractor;
import com.ruoyi.project.parse.parser.IParser;
import com.ruoyi.project.parse.parser.ParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.resource.mapper.ParseResourceMapper;
import com.ruoyi.project.system.resource.domain.ParseResource;
import com.ruoyi.project.system.resource.service.IParseResourceService;
import com.ruoyi.common.utils.text.Convert;

import static com.ruoyi.common.constant.FileConstants.SUPPORT_FILE_SUFFIX;

/**
 * 资源Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
@Slf4j
public class ParseResourceServiceImpl implements IParseResourceService {
    @Autowired
    private ParseResourceMapper parseResourceMapper;

    @Autowired
    private ParseResourceFileMapper parseResourceFileMapper;

    @Autowired
    private ParseConfigMapper parseConfigMapper;

    @Autowired
    private ParseResultMapper parseResultMapper;

    /**
     * 查询资源
     *
     * @param resourceId 资源主键
     * @return 资源
     */
    @Override
    public ParseResource selectParseResourceByResourceId(Long resourceId) {
        return parseResourceMapper.selectParseResourceByResourceId(resourceId);
    }

    /**
     * 查询资源列表
     *
     * @param parseResource 资源
     * @return 资源
     */
    @Override
    public List<ParseResource> selectParseResourceList(ParseResource parseResource) {
        return parseResourceMapper.selectParseResourceList(parseResource);
    }

    /**
     * 新增资源
     *
     * @param parseResource 资源
     * @return 结果
     */
    @Override
    public int insertParseResource(ParseResource parseResource, String packagefileName) {
        int i = parseResourceMapper.insertParseResource(parseResource);
        parseResource.setCreateTime(DateUtils.getNowDate());
        // 本地资源路径
        List<String> files = obtainFile(RuoYiConfig.getProfile() + packagefileName);
        List<ParseResourceFile> list = Lists.newArrayList();
        files.forEach(file -> {
            ParseResourceFile resourceFile = new ParseResourceFile();
            resourceFile.setResourceId(parseResource.getResourceId());
            resourceFile.setFileName(FileUtils.getName(file));
            resourceFile.setFileType(FileTypeUtils.getFileType(file));
            resourceFile.setLocation(file);
            resourceFile.setIsParsed(0L);
            list.add(resourceFile);
            parseResourceFileMapper.insertParseResourceFile(resourceFile);
        });
        return i;
    }

    private List<String> obtainFile(String absolutePath) {
        try {
            if (absolutePath.contains(".zip")) {
                return getAllFile(absolutePath);
            } else if (CharSequenceUtil.containsAny(absolutePath, SUPPORT_FILE_SUFFIX)) {
                return Lists.newArrayList(absolutePath);
            } else if (absolutePath.contains(".rar")) {
                File zdir = new File(absolutePath.substring(0, absolutePath.indexOf(".")));
                if (!zdir.isDirectory()) {
                    zdir.mkdir();
                }
                return Junrar.extract(absolutePath, zdir.getAbsolutePath()).stream().map(File::getAbsolutePath)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<String> getAllFile(String absolutePath) {
        File unzip = ZipUtil.unzip(absolutePath, Charset.forName("GBK"));
        try (Stream<Path> paths = Files.walk(Paths.get(unzip.getPath()))) {
            return paths.filter(Files::isRegularFile).map(r -> r.toFile().getAbsolutePath())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 修改资源
     *
     * @param parseResource 资源
     * @return 结果
     */
    @Override
    public int updateParseResource(ParseResource parseResource) {
        parseResource.setUpdateTime(DateUtils.getNowDate());
        return parseResourceMapper.updateParseResource(parseResource);
    }

    /**
     * 批量删除资源
     *
     * @param resourceIds 需要删除的资源主键
     * @return 结果
     */
    @Override
    public int deleteParseResourceByResourceIds(String resourceIds) {
        return parseResourceMapper.deleteParseResourceByResourceIds(Convert.toStrArray(resourceIds));
    }

    /**
     * 删除资源信息
     *
     * @param resourceId 资源主键
     * @return 结果
     */
    @Override
    public int deleteParseResourceByResourceId(Long resourceId) {
        return parseResourceMapper.deleteParseResourceByResourceId(resourceId);
    }

    /**
     * 解析资源
     *
     * @param resourceId 资源主键
     */
    @Override
    public int parseResource(Long resourceId) {
        ParseResource parseResource = parseResourceMapper.selectParseResourceByResourceId(resourceId);
        // 先删除之前解析结果
        clearPrevResult(parseResource.getResourceId());
        // 进行解析
        parse(parseResource);
        parseResource.setIsParsed(Long.parseLong(Constants.YES));
        parseResource.setUpdateTime(DateUtils.getNowDate());
        return parseResourceMapper.updateParseResource(parseResource);
    }

    public void clearPrevResult(Long resourceId) {
        ParseResourceFile parseResourceFile = new ParseResourceFile();
        parseResourceFile.setResourceId(resourceId);
        List<ParseResourceFile> parseResourceFiles = parseResourceFileMapper.selectList(parseResourceFile);

        ParseResult parseResult = new ParseResult();
        parseResult.setResourceId(resourceId);
        List<ParseResult> parseResults = parseResultMapper.selectList(parseResult);

//        // 删除文件资源
//        String[] fileIdArr = parseResourceFiles.stream().map(ParseResourceFile::getResourceFileId).map(String::valueOf).toArray(String[]::new);
//        if (fileIdArr.length > 0) {
//            parseResourceFileMapper.deleteParseResourceFileByResourceFileIds(fileIdArr);
//        }

        // 删除解析结果
        String[] array = parseResults.stream().map(ParseResult::getParseResultId).map(String::valueOf).toArray(String[]::new);
        if (array.length > 0) {
            parseResultMapper.deleteParseResultByParseResultIds(array);
        }
    }


    public void parse(ParseResource parseResource) {
//        String location = parseResource.getLocation();
//        // 创建 File 对象
//        File directory = new File(location);
//        // 检查目录是否存在
//        if (!directory.exists()) {
//            throw new ServiceException("指定的目录不存在: " + location);
//        }
//        // 获取目录下的所有文件和子目录
//        File[] files = directory.listFiles();
//        // 检查是否有文件或子目录
//        if (files == null || files.length == 0) {
//            throw new ServiceException("目录为空: " + location);
//        }
        // 获得所有子资源
        Long resourceId = parseResource.getResourceId();
        ParseResourceFile parseResourceFile = new ParseResourceFile();
        parseResourceFile.setResourceId(resourceId);
        List<ParseResourceFile> parseResourceFiles = parseResourceFileMapper.selectList(parseResourceFile);

        // 根据文件资源 获取对应的解析配置
        ParseConfig parseConfigReq = new ParseConfig();
        parseConfigReq.setResourceId(resourceId);
        List<ParseConfig> parseConfigList = parseConfigMapper.selectList(parseConfigReq);
        if (CollUtil.isEmpty(parseConfigList)) {
            throw new ServiceException("未配置解析规则");
        }
        // 遍历文件和子目录
        parseResourceFiles.forEach(r -> doParse(r, parseConfigList));
    }

    public void doParse(ParseResourceFile parseResourceFile, List<ParseConfig> parseConfigList) {
        String filePath = parseResourceFile.getLocation();
        for (ParseConfig parseConfig : parseConfigList) {
            FileFetcher fileFetcher = new FileFetcher() {
                @Override
                public List<String> fetchFiles() {
                    return ListUtil.of(filePath);
                }
                @Override
                public String fetchFile() {
                    return filePath;
                }
            };
            ExtractorConfig extractorConfig = ConfigConverter.convertToExtractorConfig(parseConfig);
            DocumentProcessor documentProcessor = new DocumentProcessor(2);
            List<ExtractedResult> extractedResults = documentProcessor.processFiles(fileFetcher, extractorConfig);
            for (ExtractedResult extractedResult : extractedResults) {
                parseResult(parseResourceFile, parseConfig, extractedResult);
            }
        }
        parseResourceFile.setIsParsed(Long.parseLong(Constants.YES));
        parseResourceFile.setUpdateTime(DateUtils.getNowDate());
        parseResourceFileMapper.updateParseResourceFile(parseResourceFile);
    }

    public void parseResult(ParseResourceFile parseResourceFile, ParseConfig parseConfig, Object parseResult) {
        log.info(String.valueOf(parseResult.toString()));
        ParseResult result = new ParseResult();
        result.setResourceId(parseConfig.getResourceId());
        result.setParseConfigId(parseConfig.getParseConfigId());
        result.setResourceFileId(parseResourceFile.getResourceFileId());
        if (Objects.nonNull(parseResult)) {
            result.setResult(JSONUtil.toJsonStr(parseResult));
        }
        parseResultMapper.insertParseResult(result);
    }

}
