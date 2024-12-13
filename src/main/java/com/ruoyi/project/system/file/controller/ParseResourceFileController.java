package com.ruoyi.project.system.file.controller;

import java.util.List;

import com.ruoyi.project.system.file.domain.ParseResourceFileDTO;
import com.ruoyi.project.system.resource.domain.ParseResource;
import com.ruoyi.project.system.resource.service.IParseResourceService;
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
import com.ruoyi.project.system.file.domain.ParseResourceFile;
import com.ruoyi.project.system.file.service.IParseResourceFileService;
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
public class ParseResourceFileController extends BaseController
{
    private String prefix = "system/file";

    @Autowired
    private IParseResourceFileService parseResourceFileService;

    @Autowired
    private IParseResourceService parseResourceService;

    @RequiresPermissions("system:file:view")
    @GetMapping()
    public String file( ModelMap mmap)
    {
        mmap.put("resources", parseResourceService.selectParseResourceList(new ParseResource()));
        startPage();
        return prefix + "/file";
    }

    /**
     * 查询文件资源列表
     */
    @RequiresPermissions("system:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseResourceFileDTO parseResourceFile)
    {
        startPage();
        List<ParseResourceFileDTO> list = parseResourceFileService.selectParseResourceFileList(parseResourceFile);
        return getDataTable(list);
    }

    /**
     * 导出文件资源列表
     */
    @RequiresPermissions("system:file:export")
    @Log(title = "文件资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseResourceFile parseResourceFile)
    {
        List<ParseResourceFile> list = parseResourceFileService.selectList(parseResourceFile);
        ExcelUtil<ParseResourceFile> util = new ExcelUtil<ParseResourceFile>(ParseResourceFile.class);
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
    public AjaxResult addSave(ParseResourceFile parseResourceFile)
    {
        return toAjax(parseResourceFileService.insertParseResourceFile(parseResourceFile));
    }

    /**
     * 修改文件资源
     */
    @RequiresPermissions("system:file:edit")
    @GetMapping("/edit/{resourceFileId}")
    public String edit(@PathVariable("resourceFileId") Long resourceFileId, ModelMap mmap)
    {
        ParseResourceFile parseResourceFile = parseResourceFileService.selectParseResourceFileByResourceFileId(resourceFileId);
        mmap.put("parseResourceFile", parseResourceFile);
        return prefix + "/edit";
    }

    /**
     * 修改保存文件资源
     */
    @RequiresPermissions("system:file:edit")
    @Log(title = "文件资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseResourceFile parseResourceFile)
    {
        return toAjax(parseResourceFileService.updateParseResourceFile(parseResourceFile));
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
        return toAjax(parseResourceFileService.deleteParseResourceFileByResourceFileIds(ids));
    }
}
