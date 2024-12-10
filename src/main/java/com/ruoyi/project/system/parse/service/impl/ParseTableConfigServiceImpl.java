package com.ruoyi.project.system.parse.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.parse.mapper.ParseTableConfigMapper;
import com.ruoyi.project.system.parse.service.IParseTableConfigService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 表格配置Service业务层处理
 *
 * @author zhaochenliang
 * @date 2024-12-06
 */
@Service
@Slf4j
public class ParseTableConfigServiceImpl implements IParseTableConfigService {
    @Autowired
    private ParseTableConfigMapper parseTableConfigMapper;

    /**
     * 查询格配置
     *
     * @param tableConfigId 格配置主键
     * @return 格配置
     */
    @Override
    public ParseTableConfig selectParseTableConfigByTableConfigId(Long tableConfigId) {
        return parseTableConfigMapper.selectParseTableConfigByTableConfigId(tableConfigId);
    }

    /**
     * 查询格配置列表
     *
     * @param parseTableConfig 格配置
     * @return 格配置
     */
    @Override
    public List<ParseTableConfig> selectParseTableConfigList(ParseTableConfig parseTableConfig) {
        return parseTableConfigMapper.selectParseTableConfigList(parseTableConfig);
    }

    /**
     * 新增格配置
     *
     * @param parseTableConfig 格配置
     * @return 结果
     */
    @Override
    public int insertParseTableConfig(ParseTableConfig parseTableConfig) {
        parseTableConfig.setCreateTime(DateUtils.getNowDate());
        return parseTableConfigMapper.insertParseTableConfig(parseTableConfig);
    }

    /**
     * 修改格配置
     *
     * @param parseTableConfig 格配置
     * @return 结果
     */
    @Override
    public int updateParseTableConfig(ParseTableConfig parseTableConfig) {
        parseTableConfig.setUpdateTime(DateUtils.getNowDate());
        return parseTableConfigMapper.updateParseTableConfig(parseTableConfig);
    }

    /**
     * 批量删除格配置
     *
     * @param tableConfigIds 需要删除的格配置主键
     * @return 结果
     */
    @Override
    public int deleteParseTableConfigByTableConfigIds(String tableConfigIds) {
        return parseTableConfigMapper.deleteParseTableConfigByTableConfigIds(Convert.toStrArray(tableConfigIds));
    }

    /**
     * 删除格配置信息
     *
     * @param tableConfigId 格配置主键
     * @return 结果
     */
    @Override
    public int deleteParseTableConfigByTableConfigId(Long tableConfigId) {
        return parseTableConfigMapper.deleteParseTableConfigByTableConfigId(tableConfigId);
    }

//    /**
//     * 解析资源
//     *
//     * @param recourseId 资源主键
//     */
//    public int parseTableConfigByRecourseId(Long recourseId) {
//        return parseTableConfigMapper.selectParseTableConfigByTableConfigId(recourseId);
//    }
//
//
//    public void parse(ParseRecourse parseRecourse, List<ParseTableConfig> tableConfigList) {
//        String location = parseRecourse.getLocation();
//        Long parserType = parseRecourse.getParserType();
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
//
//        // 遍历文件和子目录
//        for (File file : files) {
//            SpirePDFTableParser parser = new SpirePDFTableParser();
//            List<PDFTable> tableList = parser.parse(file.getAbsolutePath());
//            List<Pair<String, TableParseRule<?>>> pairList = TableExtractorConvertor.convert(tableConfigList);
//            for (Pair<String, TableParseRule<?>> pair : pairList) {
//                String key = pair.getKey();
//                TableParseRule<?> parseRule = pair.getValue();
//                Object execute = parseRule.execute(tableList);
//                parseResult(parseRecourse, key, execute);
//            }
//        }
//    }
//
//    public void parseResult(ParseRecourse parseRecourse, String tableConfig, Object parseResult) {
//        System.out.println(parseRecourse.getResourceTitle());
//        System.out.println(tableConfig);
//        System.out.println(parseResult);
//    }
}
