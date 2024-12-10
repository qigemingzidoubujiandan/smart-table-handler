package com.ruoyi.project.system.recourse.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.github.junrar.Junrar;
import com.google.common.collect.Lists;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.file.domain.ParseRecourseFile;
import com.ruoyi.project.system.file.mapper.ParseRecourseFileMapper;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.mapper.ParseConfigMapper;
import com.ruoyi.project.tool.parse.domain.ParseTypeEnum;
import com.ruoyi.project.tool.parse.domain.TableMatchMethodEnum;
import com.ruoyi.project.tool.parse.extractor.ExtractorConvertor;
import com.ruoyi.project.tool.parse.extractor.IExtractor;
import com.ruoyi.project.tool.parse.parser.IParser;
import com.ruoyi.project.tool.parse.parser.ParserFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.recourse.mapper.ParseRecourseMapper;
import com.ruoyi.project.system.recourse.domain.ParseRecourse;
import com.ruoyi.project.system.recourse.service.IParseRecourseService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 资源Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
public class ParseRecourseServiceImpl implements IParseRecourseService {
    @Autowired
    private ParseRecourseMapper parseRecourseMapper;

    @Autowired
    private ParseRecourseFileMapper parseRecourseFileMapper;

    @Autowired
    private ParseConfigMapper parseConfigMapper;

    /**
     * 查询资源
     *
     * @param resourceId 资源主键
     * @return 资源
     */
    @Override
    public ParseRecourse selectParseRecourseByResourceId(Long resourceId) {
        return parseRecourseMapper.selectParseRecourseByResourceId(resourceId);
    }

    /**
     * 查询资源列表
     *
     * @param parseRecourse 资源
     * @return 资源
     */
    @Override
    public List<ParseRecourse> selectParseRecourseList(ParseRecourse parseRecourse) {
        return parseRecourseMapper.selectParseRecourseList(parseRecourse);
    }

    /**
     * 新增资源
     *
     * @param parseRecourse 资源
     * @return 结果
     */
    @Override
    public int insertParseRecourse(ParseRecourse parseRecourse, String packagefileName) {
        int i = parseRecourseMapper.insertParseRecourse(parseRecourse);
        parseRecourse.setCreateTime(DateUtils.getNowDate());
        // 本地资源路径
        List<String> files = obtainFile(RuoYiConfig.getProfile() + packagefileName);
        List<ParseRecourseFile> list = Lists.newArrayList();
        files.forEach(file -> {
            ParseRecourseFile recourseFile = new ParseRecourseFile();
            recourseFile.setResourceId(parseRecourse.getResourceId());
            recourseFile.setFileName(file);
            recourseFile.setFileType(FileTypeUtils.getFileType(file));
            recourseFile.setLocation(file);
            recourseFile.setIsParsed(0L);
            list.add(recourseFile);
            parseRecourseFileMapper.insertParseRecourseFile(recourseFile);
        });
        return i;
    }

    private List<String> obtainFile(String absolutePath) {
        try {
            if (absolutePath.contains(".zip")) {
                return getAllFile(absolutePath);
            } else if (absolutePath.contains(".pdf")) {
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
     * @param parseRecourse 资源
     * @return 结果
     */
    @Override
    public int updateParseRecourse(ParseRecourse parseRecourse) {
        parseRecourse.setUpdateTime(DateUtils.getNowDate());
        return parseRecourseMapper.updateParseRecourse(parseRecourse);
    }

    /**
     * 批量删除资源
     *
     * @param resourceIds 需要删除的资源主键
     * @return 结果
     */
    @Override
    public int deleteParseRecourseByResourceIds(String resourceIds) {
        return parseRecourseMapper.deleteParseRecourseByResourceIds(Convert.toStrArray(resourceIds));
    }

    /**
     * 删除资源信息
     *
     * @param resourceId 资源主键
     * @return 结果
     */
    @Override
    public int deleteParseRecourseByResourceId(Long resourceId) {
        return parseRecourseMapper.deleteParseRecourseByResourceId(resourceId);
    }

    /**
     * 解析资源
     *
     * @param recourseId 资源主键
     */
    @Override
    public boolean parseResource(Long recourseId) {
        ParseRecourse parseRecourse = parseRecourseMapper.selectParseRecourseByResourceId(recourseId);
        parse(parseRecourse);
        return true;
    }


    public void parse(ParseRecourse parseRecourse) {
//        String location = parseRecourse.getLocation();
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
        ParseRecourseFile parseRecourseFile = new ParseRecourseFile();
        parseRecourseFile.setResourceId(parseRecourse.getResourceId());
        List<ParseRecourseFile> parseRecourseFiles = parseRecourseFileMapper.selectParseRecourseFileList(parseRecourseFile);

        // 根据文件资源 获取对应的解析配置
        ParseConfig parseConfigReq = new ParseConfig();
        parseConfigReq.setResourceId(parseConfigReq.getResourceId());
        List<ParseConfig> parseConfigList = parseConfigMapper.selectParseConfigList(parseConfigReq);
        // 遍历文件和子目录
        parseRecourseFiles.forEach(r -> doParse(r, parseConfigList));
    }

    public void doParse(ParseRecourseFile parseRecourseFile, List<ParseConfig> parseConfigList) {
        String filePath = parseRecourseFile.getLocation();
        for (ParseConfig parseConfig : parseConfigList) {
            // 1.获取解析器->解析
            ParseTypeEnum parseTypeEnum = ParseTypeEnum.get(parseConfig.getConfigType().intValue());
            IParser iParser = ParserFactory.createParserByFilePath(parseRecourseFile.getLocation(), parseTypeEnum);
            Object parsed = iParser.parse(filePath);

            // 2.获取抽取器->抽取
            Pair<String, ? extends IExtractor<?, ?>> pair;
            if (parseConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.KV.getCode()) {
                pair = ExtractorConvertor.convertToMapExtractor(parseConfig);
            } else if (parseConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.LIST.getCode()) {
                pair = ExtractorConvertor.convertToListExtractor(parseConfig);
            } else {
                pair = ExtractorConvertor.convertToTextExtractor(parseConfig);
            }

            // 3.对结果进行处理
            String key = pair.getKey();
            IExtractor<?, ?> extractor = pair.getValue();
            Object result = ((IExtractor<Object, ?>) extractor).extract(parsed);
            parseResult(parseRecourseFile, parseConfig, result);
        }
    }

    public void parseResult(ParseRecourseFile parseRecourseFile, ParseConfig parseConfig, Object parseResult) {
        System.out.println(parseRecourseFile);
        System.out.println(parseConfig);
        System.out.println(parseResult);
        // todo:

    }
}
