package com.ruoyi.project.system.file.service;

import java.util.List;
import com.ruoyi.project.system.file.domain.ParseResourceFile;
import com.ruoyi.project.system.file.domain.ParseResourceFileDTO;

/**
 * 文件资源Service接口
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
public interface IParseResourceFileService 
{
    /**
     * 查询文件资源
     * 
     * @param resourceFileId 文件资源主键
     * @return 文件资源
     */
    public ParseResourceFile selectParseResourceFileByResourceFileId(Long resourceFileId);

    /**
     * 查询文件资源列表
     * 
     * @param parseResourceFile 文件资源
     * @return 文件资源集合
     */
    public List<ParseResourceFileDTO> selectParseResourceFileList(ParseResourceFileDTO parseResourceFile);

    /**
     * 新增文件资源
     * 
     * @param parseResourceFile 文件资源
     * @return 结果
     */
    public int insertParseResourceFile(ParseResourceFile parseResourceFile);

    /**
     * 修改文件资源
     * 
     * @param parseResourceFile 文件资源
     * @return 结果
     */
    public int updateParseResourceFile(ParseResourceFile parseResourceFile);

    /**
     * 批量删除文件资源
     * 
     * @param resourceFileIds 需要删除的文件资源主键集合
     * @return 结果
     */
    public int deleteParseResourceFileByResourceFileIds(String resourceFileIds);

    /**
     * 删除文件资源信息
     * 
     * @param resourceFileId 文件资源主键
     * @return 结果
     */
    public int deleteParseResourceFileByResourceFileId(Long resourceFileId);

    public List<ParseResourceFile> selectList(ParseResourceFile parseResourceFile);

}
