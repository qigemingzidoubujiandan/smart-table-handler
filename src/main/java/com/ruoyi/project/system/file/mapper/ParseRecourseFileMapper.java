package com.ruoyi.project.system.file.mapper;

import java.util.List;
import com.ruoyi.project.system.file.domain.ParseRecourseFile;

/**
 * 文件资源Mapper接口
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
public interface ParseRecourseFileMapper 
{
    /**
     * 查询文件资源
     * 
     * @param recourseFileId 文件资源主键
     * @return 文件资源
     */
    public ParseRecourseFile selectParseRecourseFileByRecourseFileId(Long recourseFileId);

    /**
     * 查询文件资源列表
     * 
     * @param parseRecourseFile 文件资源
     * @return 文件资源集合
     */
    public List<ParseRecourseFile> selectParseRecourseFileList(ParseRecourseFile parseRecourseFile);

    /**
     * 新增文件资源
     * 
     * @param parseRecourseFile 文件资源
     * @return 结果
     */
    public int insertParseRecourseFile(ParseRecourseFile parseRecourseFile);

    /**
     * 修改文件资源
     * 
     * @param parseRecourseFile 文件资源
     * @return 结果
     */
    public int updateParseRecourseFile(ParseRecourseFile parseRecourseFile);

    /**
     * 删除文件资源
     * 
     * @param recourseFileId 文件资源主键
     * @return 结果
     */
    public int deleteParseRecourseFileByRecourseFileId(Long recourseFileId);

    /**
     * 批量删除文件资源
     * 
     * @param recourseFileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParseRecourseFileByRecourseFileIds(String[] recourseFileIds);
}
