package com.ruoyi.project.system.file.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.file.domain.ParseResourceFileDTO;
import com.ruoyi.project.system.resource.mapper.ParseResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.file.mapper.ParseResourceFileMapper;
import com.ruoyi.project.system.file.domain.ParseResourceFile;
import com.ruoyi.project.system.file.service.IParseResourceFileService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 文件资源Service业务层处理
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Service
public class ParseResourceFileServiceImpl implements IParseResourceFileService 
{
    @Autowired
    private ParseResourceFileMapper parseResourceFileMapper;
    @Autowired
    private ParseResourceMapper parseResourceMapper;

    /**
     * 查询文件资源
     * 
     * @param resourceFileId 文件资源主键
     * @return 文件资源
     */
    @Override
    public ParseResourceFile selectParseResourceFileByResourceFileId(Long resourceFileId)
    {
        return parseResourceFileMapper.selectParseResourceFileByResourceFileId(resourceFileId);
    }

    /**
     * 查询文件资源列表
     * 
     * @param parseResourceFile 文件资源
     * @return 文件资源
     */
    @Override
    public List<ParseResourceFileDTO> selectParseResourceFileList(ParseResourceFileDTO parseResourceFile)
    {
        return parseResourceFileMapper.selectParseResourceFileList(parseResourceFile);
    }

    /**
     * 新增文件资源
     * 
     * @param parseResourceFile 文件资源
     * @return 结果
     */
    @Override
    public int insertParseResourceFile(ParseResourceFile parseResourceFile)
    {
        parseResourceFile.setCreateTime(DateUtils.getNowDate());
        return parseResourceFileMapper.insertParseResourceFile(parseResourceFile);
    }

    /**
     * 修改文件资源
     * 
     * @param parseResourceFile 文件资源
     * @return 结果
     */
    @Override
    public int updateParseResourceFile(ParseResourceFile parseResourceFile)
    {
        parseResourceFile.setUpdateTime(DateUtils.getNowDate());
        return parseResourceFileMapper.updateParseResourceFile(parseResourceFile);
    }

    /**
     * 批量删除文件资源
     * 
     * @param resourceFileIds 需要删除的文件资源主键
     * @return 结果
     */
    @Override
    public int deleteParseResourceFileByResourceFileIds(String resourceFileIds)
    {
        return parseResourceFileMapper.deleteParseResourceFileByResourceFileIds(Convert.toStrArray(resourceFileIds));
    }

    /**
     * 删除文件资源信息
     * 
     * @param resourceFileId 文件资源主键
     * @return 结果
     */
    @Override
    public int deleteParseResourceFileByResourceFileId(Long resourceFileId)
    {
        return parseResourceFileMapper.deleteParseResourceFileByResourceFileId(resourceFileId);
    }

    @Override
    public List<ParseResourceFile> selectList(ParseResourceFile parseResourceFile) {
        return parseResourceFileMapper.selectList(parseResourceFile);
    }
}
