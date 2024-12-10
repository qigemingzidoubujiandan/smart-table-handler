package com.ruoyi.project.system.file.controller;

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
import com.ruoyi.project.system.file.domain.ParseRecourseFile;
import com.ruoyi.project.system.file.service.IParseRecourseFileService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 文件资源Controller
 * 
 * @author zhaochenliang
 * @date 2024-12-10
 */
@Controller
@RequestMapping("/system/file")
public class ParseRecourseFileController extends BaseController
{
    private String prefix = "system/file";

    @Autowired
    private IParseRecourseFileService parseRecourseFileService;

    @RequiresPermissions("system:file:view")
    @GetMapping()
    public String file()
    {
        return prefix + "/file";
    }

    /**
     * 查询文件资源列表
     */
    @RequiresPermissions("system:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseRecourseFile parseRecourseFile)
    {
        startPage();
        List<ParseRecourseFile> list = parseRecourseFileService.selectParseRecourseFileList(parseRecourseFile);
        return getDataTable(list);
    }

    /**
     * 导出文件资源列表
     */
    @RequiresPermissions("system:file:export")
    @Log(title = "文件资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseRecourseFile parseRecourseFile)
    {
        List<ParseRecourseFile> list = parseRecourseFileService.selectParseRecourseFileList(parseRecourseFile);
        ExcelUtil<ParseRecourseFile> util = new ExcelUtil<ParseRecourseFile>(ParseRecourseFile.class);
        return util.exportExcel(list, "文件资源数据");
    }

    /**
     * 新增文件资源
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存文件资源
     */
    @RequiresPermissions("system:file:add")
    @Log(title = "文件资源", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ParseRecourseFile parseRecourseFile)
    {
        return toAjax(parseRecourseFileService.insertParseRecourseFile(parseRecourseFile));
    }

    /**
     * 修改文件资源
     */
    @RequiresPermissions("system:file:edit")
    @GetMapping("/edit/{recourseFileId}")
    public String edit(@PathVariable("recourseFileId") Long recourseFileId, ModelMap mmap)
    {
        ParseRecourseFile parseRecourseFile = parseRecourseFileService.selectParseRecourseFileByRecourseFileId(recourseFileId);
        mmap.put("parseRecourseFile", parseRecourseFile);
        return prefix + "/edit";
    }

    /**
     * 修改保存文件资源
     */
    @RequiresPermissions("system:file:edit")
    @Log(title = "文件资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseRecourseFile parseRecourseFile)
    {
        return toAjax(parseRecourseFileService.updateParseRecourseFile(parseRecourseFile));
    }

    /**
     * 删除文件资源
     */
    @RequiresPermissions("system:file:remove")
    @Log(title = "文件资源", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(parseRecourseFileService.deleteParseRecourseFileByRecourseFileIds(ids));
    }
}
