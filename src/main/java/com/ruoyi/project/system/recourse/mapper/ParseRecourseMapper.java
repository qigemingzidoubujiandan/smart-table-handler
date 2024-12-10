package com.ruoyi.project.system.recourse.mapper;

import java.util.List;
import com.ruoyi.project.system.recourse.domain.ParseRecourse;

/**
 * 资源Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface ParseRecourseMapper 
{
    /**
     * 查询资源
     * 
     * @param resourceId 资源主键
     * @return 资源
     */
    public ParseRecourse selectParseRecourseByResourceId(Long resourceId);

    /**
     * 查询资源列表
     * 
     * @param parseRecourse 资源
     * @return 资源集合
     */
    public List<ParseRecourse> selectParseRecourseList(ParseRecourse parseRecourse);

    /**
     * 新增资源
     * 
     * @param parseRecourse 资源
     * @return 结果
     */
    public int insertParseRecourse(ParseRecourse parseRecourse);

    /**
     * 修改资源
     * 
     * @param parseRecourse 资源
     * @return 结果
     */
    public int updateParseRecourse(ParseRecourse parseRecourse);

    /**
     * 删除资源
     * 
     * @param resourceId 资源主键
     * @return 结果
     */
    public int deleteParseRecourseByResourceId(Long resourceId);

    /**
     * 批量删除资源
     * 
     * @param resourceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParseRecourseByResourceIds(String[] resourceIds);
}
