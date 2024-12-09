package com.ruoyi.project.system.recourse.service.impl;

import java.io.File;
import java.util.List;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.parse.domain.ParseTableConfig;
import com.ruoyi.project.system.parse.mapper.ParseTableConfigMapper;
import com.ruoyi.project.system.parse.parse.domain.PDFTable;
import com.ruoyi.project.system.parse.parse.extractor.TableExtractorConvertor;
import com.ruoyi.project.system.parse.parse.extractor.TableParseRule;
import com.ruoyi.project.system.parse.parse.parser.SpirePDFTableParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.recourse.mapper.ParseRecourseMapper;
import com.ruoyi.project.system.recourse.domain.ParseRecourse;
import com.ruoyi.project.system.recourse.service.IParseRecourseService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 解析资源Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-09
 */
@Service
public class ParseRecourseServiceImpl implements IParseRecourseService {
    @Autowired
    private ParseRecourseMapper parseRecourseMapper;
    @Autowired
    private ParseTableConfigMapper parseTableConfigMapper;

    /**
     * 查询解析资源
     *
     * @param resourceId 解析资源主键
     * @return 解析资源
     */
    @Override
    public ParseRecourse selectParseRecourseByResourceId(Long resourceId) {
        return parseRecourseMapper.selectParseRecourseByResourceId(resourceId);
    }

    /**
     * 查询解析资源列表
     *
     * @param parseRecourse 解析资源
     * @return 解析资源
     */
    @Override
    public List<ParseRecourse> selectParseRecourseList(ParseRecourse parseRecourse) {
        return parseRecourseMapper.selectParseRecourseList(parseRecourse);
    }

    /**
     * 新增解析资源
     *
     * @param parseRecourse 解析资源
     * @return 结果
     */
    @Override
    public int insertParseRecourse(ParseRecourse parseRecourse) {
        parseRecourse.setCreateTime(DateUtils.getNowDate());
        return parseRecourseMapper.insertParseRecourse(parseRecourse);
    }

    /**
     * 修改解析资源
     *
     * @param parseRecourse 解析资源
     * @return 结果
     */
    @Override
    public int updateParseRecourse(ParseRecourse parseRecourse) {
        parseRecourse.setUpdateTime(DateUtils.getNowDate());
        return parseRecourseMapper.updateParseRecourse(parseRecourse);
    }

    /**
     * 批量删除解析资源
     *
     * @param resourceIds 需要删除的解析资源主键
     * @return 结果
     */
    @Override
    public int deleteParseRecourseByResourceIds(String resourceIds) {
        return parseRecourseMapper.deleteParseRecourseByResourceIds(Convert.toStrArray(resourceIds));
    }

    /**
     * 删除解析资源信息
     *
     * @param resourceId 解析资源主键
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
    public boolean parseTableConfigByRecourseId(Long recourseId) {
        ParseRecourse parseRecourse = parseRecourseMapper.selectParseRecourseByResourceId(1L);
        ParseTableConfig parseTableConfig = new ParseTableConfig();
        parseTableConfig.setResourceId(1L);
        List<ParseTableConfig> parseTableConfigs = parseTableConfigMapper.selectParseTableConfigList(parseTableConfig);

        parse(parseRecourse, parseTableConfigs);
        return true;
    }


    public void parse(ParseRecourse parseRecourse, List<ParseTableConfig> tableConfigList) {
        String location = parseRecourse.getLocation();
        Long parserType = parseRecourse.getParserType();
        // 创建 File 对象
        File directory = new File(location);
        // 检查目录是否存在
        if (!directory.exists()) {
            throw new ServiceException("指定的目录不存在: " + location);
        }
        // 获取目录下的所有文件和子目录
        File[] files = directory.listFiles();
        // 检查是否有文件或子目录
        if (files == null || files.length == 0) {
            throw new ServiceException("目录为空: " + location);
        }

        // 遍历文件和子目录
        for (File file : files) {
            SpirePDFTableParser parser = new SpirePDFTableParser();
            List<PDFTable> tableList = parser.parse(file.getAbsolutePath());
            List<Pair<String, TableParseRule<?>>> pairList = TableExtractorConvertor.convert(tableConfigList);
            for (Pair<String, TableParseRule<?>> pair : pairList) {
                String key = pair.getKey();
                TableParseRule<?> parseRule = pair.getValue();
                Object execute = parseRule.execute(tableList);
                parseResult(parseRecourse, key, execute);
            }
        }
    }

    public void parseResult(ParseRecourse parseRecourse, String tableConfig, Object parseResult) {
        System.out.println(parseRecourse.getResourceTitle());
        System.out.println(tableConfig);
        System.out.println(parseResult);
    }
}
