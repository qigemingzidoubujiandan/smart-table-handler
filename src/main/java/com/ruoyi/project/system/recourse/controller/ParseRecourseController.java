package com.ruoyi.project.system.recourse.controller;

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
import com.ruoyi.project.system.recourse.domain.ParseRecourse;
import com.ruoyi.project.system.recourse.service.IParseRecourseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 解析资源Controller
 * 
 * @author ruoyi
 * @date 2024-12-09
 */
@Controller
@RequestMapping("/system/recourse")
public class ParseRecourseController extends BaseController
{
    private String prefix = "system/recourse";

    @Autowired
    private IParseRecourseService parseRecourseService;

    @RequiresPermissions("system:recourse:view")
    @GetMapping()
    public String recourse()
    {
        return prefix + "/recourse";
    }

    /**
     * 查询解析资源列表
     */
    @RequiresPermissions("system:recourse:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseRecourse parseRecourse)
    {
        startPage();
        List<ParseRecourse> list = parseRecourseService.selectParseRecourseList(parseRecourse);
        return getDataTable(list);
    }

    /**
     * 导出解析资源列表
     */
    @RequiresPermissions("system:recourse:export")
    @Log(title = "解析资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseRecourse parseRecourse)
    {
        List<ParseRecourse> list = parseRecourseService.selectParseRecourseList(parseRecourse);
        ExcelUtil<ParseRecourse> util = new ExcelUtil<ParseRecourse>(ParseRecourse.class);
        return util.exportExcel(list, "解析资源数据");
    }

    /**
     * 新增解析资源
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存解析资源
     */
    @RequiresPermissions("system:recourse:add")
    @Log(title = "解析资源", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ParseRecourse parseRecourse)
    {
        return toAjax(parseRecourseService.insertParseRecourse(parseRecourse));
    }

    /**
     * 修改解析资源
     */
    @RequiresPermissions("system:recourse:edit")
    @GetMapping("/edit/{resourceId}")
    public String edit(@PathVariable("resourceId") Long resourceId, ModelMap mmap)
    {
        ParseRecourse parseRecourse = parseRecourseService.selectParseRecourseByResourceId(resourceId);
        mmap.put("parseRecourse", parseRecourse);
        return prefix + "/edit";
    }

    /**
     * 修改保存解析资源
     */
    @RequiresPermissions("system:recourse:edit")
    @Log(title = "解析资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseRecourse parseRecourse)
    {
        return toAjax(parseRecourseService.updateParseRecourse(parseRecourse));
    }

    /**
     * 删除解析资源
     */
    @RequiresPermissions("system:recourse:remove")
    @Log(title = "解析资源", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(parseRecourseService.deleteParseRecourseByResourceIds(ids));
    }

    /**
     * 删除解析资源
     */
    @Log(title = "解析资源", businessType = BusinessType.DELETE)
    @PostMapping( "/parse")
    @ResponseBody
    public AjaxResult parse(Long id)
    {
        return toAjax(parseRecourseService.parseTableConfigByRecourseId(id));
    }
}
