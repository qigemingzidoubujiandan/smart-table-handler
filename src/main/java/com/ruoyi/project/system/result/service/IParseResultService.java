package com.ruoyi.project.system.result.service;

import java.util.List;
import com.ruoyi.project.system.result.domain.ParseResult;

/**
 * 解析结果Service接口
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
public interface IParseResultService 
{
    /**
     * 查询解析结果
     * 
     * @param parseResultId 解析结果主键
     * @return 解析结果
     */
    public ParseResult selectParseResultByParseResultId(Long parseResultId);

    /**
     * 查询解析结果列表
     * 
     * @param parseResult 解析结果
     * @return 解析结果集合
     */
    public List<ParseResult> selectParseResultList(ParseResult parseResult);

    /**
     * 新增解析结果
     * 
     * @param parseResult 解析结果
     * @return 结果
     */
    public int insertParseResult(ParseResult parseResult);

    /**
     * 修改解析结果
     * 
     * @param parseResult 解析结果
     * @return 结果
     */
    public int updateParseResult(ParseResult parseResult);

    /**
     * 批量删除解析结果
     * 
     * @param parseResultIds 需要删除的解析结果主键集合
     * @return 结果
     */
    public int deleteParseResultByParseResultIds(String parseResultIds);

    /**
     * 删除解析结果信息
     * 
     * @param parseResultId 解析结果主键
     * @return 结果
     */
    public int deleteParseResultByParseResultId(Long parseResultId);
}
