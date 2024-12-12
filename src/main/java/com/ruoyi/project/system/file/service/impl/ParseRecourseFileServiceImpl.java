package com.ruoyi.project.system.file.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.file.domain.ParseRecourseFileDTO;
import com.ruoyi.project.system.recourse.mapper.ParseRecourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.file.mapper.ParseRecourseFileMapper;
import com.ruoyi.project.system.file.domain.ParseRecourseFile;
import com.ruoyi.project.system.file.service.IParseRecourseFileService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 文件资源Service业务层处理
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Service
public class ParseRecourseFileServiceImpl implements IParseRecourseFileService 
{
    @Autowired
    private ParseRecourseFileMapper parseRecourseFileMapper;
    @Autowired
    private ParseRecourseMapper parseRecourseMapper;

    /**
     * 查询文件资源
     * 
     * @param recourseFileId 文件资源主键
     * @return 文件资源
     */
    @Override
    public ParseRecourseFile selectParseRecourseFileByRecourseFileId(Long recourseFileId)
    {
        return parseRecourseFileMapper.selectParseRecourseFileByRecourseFileId(recourseFileId);
    }

    /**
     * 查询文件资源列表
     * 
     * @param parseRecourseFile 文件资源
     * @return 文件资源
     */
    @Override
    public List<ParseRecourseFileDTO> selectParseRecourseFileList(ParseRecourseFileDTO parseRecourseFile)
    {
        return parseRecourseFileMapper.selectParseRecourseFileList(parseRecourseFile);
    }

    /**
     * 新增文件资源
     * 
     * @param parseRecourseFile 文件资源
     * @return 结果
     */
    @Override
    public int insertParseRecourseFile(ParseRecourseFile parseRecourseFile)
    {
        parseRecourseFile.setCreateTime(DateUtils.getNowDate());
        return parseRecourseFileMapper.insertParseRecourseFile(parseRecourseFile);
    }

    /**
     * 修改文件资源
     * 
     * @param parseRecourseFile 文件资源
     * @return 结果
     */
    @Override
    public int updateParseRecourseFile(ParseRecourseFile parseRecourseFile)
    {
        parseRecourseFile.setUpdateTime(DateUtils.getNowDate());
        return parseRecourseFileMapper.updateParseRecourseFile(parseRecourseFile);
    }

    /**
     * 批量删除文件资源
     * 
     * @param recourseFileIds 需要删除的文件资源主键
     * @return 结果
     */
    @Override
    public int deleteParseRecourseFileByRecourseFileIds(String recourseFileIds)
    {
        return parseRecourseFileMapper.deleteParseRecourseFileByRecourseFileIds(Convert.toStrArray(recourseFileIds));
    }

    /**
     * 删除文件资源信息
     * 
     * @param recourseFileId 文件资源主键
     * @return 结果
     */
    @Override
    public int deleteParseRecourseFileByRecourseFileId(Long recourseFileId)
    {
        return parseRecourseFileMapper.deleteParseRecourseFileByRecourseFileId(recourseFileId);
    }

    @Override
    public List<ParseRecourseFile> selectList(ParseRecourseFile parseRecourseFile) {
        return parseRecourseFileMapper.selectList(parseRecourseFile);
    }
}
