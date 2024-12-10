//package com.ruoyi.project.tool.parse.parser;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.ruoyi.common.exception.ServiceException;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.project.system.parse.domain.ParseConfig;
//import com.ruoyi.project.system.parse.domain.ParseRecourse;
//import com.ruoyi.project.system.parse.domain.ParseRecourseFile;
//import com.ruoyi.project.system.parse.mapper.ParseTableConfigMapper;
//import com.ruoyi.project.tool.parse.domain.ParseTypeEnum;
//import com.ruoyi.project.tool.parse.domain.TableMatchMethodEnum;
//import com.ruoyi.project.tool.parse.extractor.ExtractorConvertor;
//import com.ruoyi.project.tool.parse.extractor.IExtractor;
//import org.apache.commons.lang3.tuple.Pair;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.ruoyi.project.system.recourse.mapper.ParseRecourseMapper;
//import com.ruoyi.project.system.recourse.service.IParseRecourseService;
//import com.ruoyi.common.utils.text.Convert;
//
///**
// * 解析资源Service业务层处理
// *
// * @author ruoyi
// * @date 2024-12-09
// */
//@Service
//public class ParseRecourseServiceImpl implements IParseRecourseService {
//    @Autowired
//    private ParseRecourseMapper parseRecourseMapper;
//    @Autowired
//    private ParseTableConfigMapper parseTableConfigMapper;
//
//    /**
//     * 查询解析资源
//     *
//     * @param resourceId 解析资源主键
//     * @return 解析资源
//     */
//    @Override
//    public ParseRecourse selectParseRecourseByResourceId(Long resourceId) {
//        return parseRecourseMapper.selectParseRecourseByResourceId(resourceId);
//    }
//
//    /**
//     * 查询解析资源列表
//     *
//     * @param parseRecourse 解析资源
//     * @return 解析资源
//     */
//    @Override
//    public List<ParseRecourse> selectParseRecourseList(ParseRecourse parseRecourse) {
//        return parseRecourseMapper.selectParseRecourseList(parseRecourse);
//    }
//
//    /**
//     * 新增解析资源
//     *
//     * @param parseRecourse 解析资源
//     * @return 结果
//     */
//    @Override
//    public int insertParseRecourse(ParseRecourse parseRecourse) {
//        parseRecourse.setCreateTime(DateUtils.getNowDate());
//        return parseRecourseMapper.insertParseRecourse(parseRecourse);
//    }
//
//    /**
//     * 修改解析资源
//     *
//     * @param parseRecourse 解析资源
//     * @return 结果
//     */
//    @Override
//    public int updateParseRecourse(ParseRecourse parseRecourse) {
//        parseRecourse.setUpdateTime(DateUtils.getNowDate());
//        return parseRecourseMapper.updateParseRecourse(parseRecourse);
//    }
//
//    /**
//     * 批量删除解析资源
//     *
//     * @param resourceIds 需要删除的解析资源主键
//     * @return 结果
//     */
//    @Override
//    public int deleteParseRecourseByResourceIds(String resourceIds) {
//        return parseRecourseMapper.deleteParseRecourseByResourceIds(Convert.toStrArray(resourceIds));
//    }
//
//    /**
//     * 删除解析资源信息
//     *
//     * @param resourceId 解析资源主键
//     * @return 结果
//     */
//    @Override
//    public int deleteParseRecourseByResourceId(Long resourceId) {
//        return parseRecourseMapper.deleteParseRecourseByResourceId(resourceId);
//    }
//
//
//    /**
//     * 解析资源
//     *
//     * @param recourseId 资源主键
//     */
//    @Override
//    public boolean parseTableConfigByRecourseId(Long recourseId) {
//        ParseRecourse parseRecourse = parseRecourseMapper.selectParseRecourseByResourceId(1L);
//        ParseConfig parseTableConfig = new ParseConfig();
//        parseTableConfig.setResourceId(1L);
//        List<ParseConfig> parseTableConfigs = null;//parseTableConfigMapper.selectParseTableConfigList(parseTableConfig);
//
//        parse(parseRecourse, parseTableConfigs);
//        return true;
//    }
//
//
//    public void parse(ParseRecourse parseRecourse, List<ParseConfig> tableConfigList) {
//        String location = parseRecourse.getLocation();
//
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
//        // todo 解析入子表
//        // 获得所有子资源
//        List<ParseRecourseFile> parseRecourseFiles = new ArrayList<>();
//        // 遍历文件和子目录
//        parseRecourseFiles.forEach(this::doParse);
//    }
//
//    public void doParse(ParseRecourseFile parseRecourseFile) {
//        String filePath = parseRecourseFile.getLocation();
//        // 根据文件资源 获取对应的解析配置
//        List<ParseConfig> parseConfigList = new ArrayList<>();
//        for (ParseConfig parseConfig : parseConfigList) {
//            ParseTypeEnum parseTypeEnum = ParseTypeEnum.get(parseConfig.getConfigType());
//            IParser iParser = ParserFactory.createParserByFilePath(parseRecourseFile.getLocation(), parseTypeEnum);
//            Object parsed = iParser.parse(filePath);
//            Pair<String, ? extends IExtractor<?, ?>> pair;
//            if (parseConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.KV.getCode()) {
//                pair = ExtractorConvertor.convertToMapExtractor(parseConfig);
//            } else if (parseConfig.getTableMatchMethod().intValue() == TableMatchMethodEnum.LIST.getCode()) {
//                pair = ExtractorConvertor.convertToListExtractor(parseConfig);
//            } else {
//                pair = ExtractorConvertor.convertToTextExtractor(parseConfig);
//            }
//            String key = pair.getKey();
//            IExtractor<?, ?> extractor = pair.getValue();
//            Object result = ((IExtractor<Object, ?>) extractor).extract(parsed);
//            parseResult(parseRecourseFile, parseConfig.getParseConfigDesc(), result);
//        }
//
//    }
//
//    public void parseResult(ParseRecourseFile parseRecourseFile, String desc, Object parseResult) {
//        System.out.println(parseRecourseFile);
//        System.out.println(desc);
//        System.out.println(parseResult);
//    }
//}
