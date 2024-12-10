package com.ruoyi.project.system.result.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.result.domain.ParseResult;
import com.ruoyi.project.system.result.service.IParseResultService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 解析结果Controller
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Controller
@RequestMapping("/system/result")
public class ParseResultController extends BaseController
{
    private String prefix = "system/result";

    @Autowired
    private IParseResultService parseResultService;

    @RequiresPermissions("system:result:view")
    @GetMapping()
    public String result()
    {
        return prefix + "/result";
    }

    /**
     * 查询解析结果列表
     */
    @RequiresPermissions("system:result:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseResult parseResult)
    {
        startPage();
        List<ParseResult> list = parseResultService.selectParseResultList(parseResult);
        return getDataTable(list);
    }

    /**
     * 导出解析结果列表
     */
    @RequiresPermissions("system:result:export")
    @Log(title = "解析结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseResult parseResult)
    {
        List<ParseResult> list = parseResultService.selectParseResultList(parseResult);
        ExcelUtil<ParseResult> util = new ExcelUtil<ParseResult>(ParseResult.class);
        return util.exportExcel(list, "解析结果数据");
    }

    /**
     * 新增解析结果
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存解析结果
     */
    @RequiresPermissions("system:result:add")
    @Log(title = "解析结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ParseResult parseResult)
    {
        return toAjax(parseResultService.insertParseResult(parseResult));
    }

    /**
     * 修改解析结果
     */
    @RequiresPermissions("system:result:edit")
    @GetMapping("/edit/{parseResultId}")
    public String edit(@PathVariable("parseResultId") Long parseResultId, ModelMap mmap)
    {
        ParseResult parseResult = parseResultService.selectParseResultByParseResultId(parseResultId);
        mmap.put("parseResult", parseResult);
        return prefix + "/edit";
    }

    /**
     * 修改保存解析结果
     */
    @RequiresPermissions("system:result:edit")
    @Log(title = "解析结果", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseResult parseResult)
    {
        return toAjax(parseResultService.updateParseResult(parseResult));
    }

    /**
     * 删除解析结果
     */
    @RequiresPermissions("system:result:remove")
    @Log(title = "解析结果", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(parseResultService.deleteParseResultByParseResultIds(ids));
    }
}
