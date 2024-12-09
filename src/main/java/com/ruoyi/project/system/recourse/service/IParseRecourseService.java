package com.ruoyi.project.system.recourse.service;

import java.util.List;

import com.ruoyi.project.system.recourse.domain.ParseRecourse;

/**
 * 解析资源Service接口
 *
 * @author ruoyi
 * @date 2024-12-09
 */
public interface IParseRecourseService {
    /**
     * 查询解析资源
     *
     * @param resourceId 解析资源主键
     * @return 解析资源
     */
    public ParseRecourse selectParseRecourseByResourceId(Long resourceId);

    /**
     * 查询解析资源列表
     *
     * @param parseRecourse 解析资源
     * @return 解析资源集合
     */
    public List<ParseRecourse> selectParseRecourseList(ParseRecourse parseRecourse);

    /**
     * 新增解析资源
     *
     * @param parseRecourse 解析资源
     * @return 结果
     */
    public int insertParseRecourse(ParseRecourse parseRecourse);

    /**
     * 修改解析资源
     *
     * @param parseRecourse 解析资源
     * @return 结果
     */
    public int updateParseRecourse(ParseRecourse parseRecourse);

    /**
     * 批量删除解析资源
     *
     * @param resourceIds 需要删除的解析资源主键集合
     * @return 结果
     */
    public int deleteParseRecourseByResourceIds(String resourceIds);

    /**
     * 删除解析资源信息
     *
     * @param resourceId 解析资源主键
     * @return 结果
     */
    public int deleteParseRecourseByResourceId(Long resourceId);

    public boolean parseTableConfigByRecourseId(Long recourseId);

}
