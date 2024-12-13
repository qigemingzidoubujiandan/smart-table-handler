package com.ruoyi.project.system.result.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.result.domain.ParseResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.result.mapper.ParseResultMapper;
import com.ruoyi.project.system.result.domain.ParseResult;
import com.ruoyi.project.system.result.service.IParseResultService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 解析结果Service业务层处理
 *
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Service
public class ParseResultServiceImpl implements IParseResultService {
    @Autowired
    private ParseResultMapper parseResultMapper;

    /**
     * 查询解析结果
     *
     * @param parseResultId 解析结果主键
     * @return 解析结果
     */
    @Override
    public ParseResult selectParseResultByParseResultId(Long parseResultId) {
        return parseResultMapper.selectParseResultByParseResultId(parseResultId);
    }

    /**
     * 查询解析结果列表
     *
     * @param parseResult 解析结果
     * @return 解析结果
     */
    @Override
    public List<ParseResultDTO> selectParseResultList(ParseResult parseResult) {
        return parseResultMapper.selectParseResultList(parseResult);
    }

    /**
     * 查询解析结果列表
     *
     * @param parseResult 解析结果
     * @return 解析结果
     */
    @Override
    public List<ParseResult> selectList(ParseResult parseResult) {
        return parseResultMapper.selectList(parseResult);
    }

    /**
     * 新增解析结果
     *
     * @param parseResult 解析结果
     * @return 结果
     */
    @Override
    public int insertParseResult(ParseResult parseResult) {
        parseResult.setCreateTime(DateUtils.getNowDate());
        return parseResultMapper.insertParseResult(parseResult);
    }

    /**
     * 修改解析结果
     *
     * @param parseResult 解析结果
     * @return 结果
     */
    @Override
    public int updateParseResult(ParseResult parseResult) {
        parseResult.setUpdateTime(DateUtils.getNowDate());
        return parseResultMapper.updateParseResult(parseResult);
    }

    /**
     * 批量删除解析结果
     *
     * @param parseResultIds 需要删除的解析结果主键
     * @return 结果
     */
    @Override
    public int deleteParseResultByParseResultIds(String parseResultIds) {
        return parseResultMapper.deleteParseResultByParseResultIds(Convert.toStrArray(parseResultIds));
    }

    /**
     * 删除解析结果信息
     *
     * @param parseResultId 解析结果主键
     * @return 结果
     */
    @Override
    public int deleteParseResultByParseResultId(Long parseResultId) {
        return parseResultMapper.deleteParseResultByParseResultId(parseResultId);
    }
}
