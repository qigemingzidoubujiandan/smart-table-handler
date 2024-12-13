package com.ruoyi.project.system.resource.mapper;

import java.util.List;
import com.ruoyi.project.system.resource.domain.ParseResource;

/**
 * 资源Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface ParseResourceMapper 
{
    /**
     * 查询资源
     * 
     * @param resourceId 资源主键
     * @return 资源
     */
    public ParseResource selectParseResourceByResourceId(Long resourceId);

    /**
     * 查询资源列表
     * 
     * @param parseResource 资源
     * @return 资源集合
     */
    public List<ParseResource> selectParseResourceList(ParseResource parseResource);

    /**
     * 新增资源
     * 
     * @param parseResource 资源
     * @return 结果
     */
    public int insertParseResource(ParseResource parseResource);

    /**
     * 修改资源
     * 
     * @param parseResource 资源
     * @return 结果
     */
    public int updateParseResource(ParseResource parseResource);

    /**
     * 删除资源
     * 
     * @param resourceId 资源主键
     * @return 结果
     */
    public int deleteParseResourceByResourceId(Long resourceId);

    /**
     * 批量删除资源
     * 
     * @param resourceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteParseResourceByResourceIds(String[] resourceIds);
}
