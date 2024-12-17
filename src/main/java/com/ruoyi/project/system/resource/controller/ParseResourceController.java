package com.ruoyi.project.system.resource.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.project.monitor.job.domain.Job;
import com.ruoyi.project.parse.util.DynamicClassGenerator;
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.service.IParseConfigService;
import javassist.CtClass;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.resource.domain.ParseResource;
import com.ruoyi.project.system.resource.service.IParseResourceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源Controller
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Controller
@RequestMapping("/system/resource")
public class ParseResourceController extends BaseController {
    private String prefix = "system/resource";

    @Autowired
    private IParseResourceService parseResourceService;

    @Autowired
    private IParseConfigService parseConfigService;
    @Autowired
    private ServerConfig serverConfig;

    @GetMapping()
    public String resource() {
        return prefix + "/resource";
    }

    /**
     * 查询资源列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseResource parseResource) {
        startPage();
        List<ParseResource> list = parseResourceService.selectParseResourceList(parseResource);
        return getDataTable(list);
    }

    /**
     * 导出资源列表
     */
    @Log(title = "资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseResource parseResource) {
        List<ParseResource> list = parseResourceService.selectParseResourceList(parseResource);
        ExcelUtil<ParseResource> util = new ExcelUtil<ParseResource>(ParseResource.class);
        return util.exportExcel(list, "资源数据");
    }

    /**
     * 新增资源
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存资源（含文件上传）
     */
    @Log(title = "资源", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("file") MultipartFile file,
                              @RequestParam("resourceDesc") String resourceDesc,
                              @RequestParam("remark") String remark) throws IOException {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String upload = FileUploadUtils.upload(filePath, file);
        upload = upload.replaceAll("/profile", "");
        // 构建资源对象并保存到数据库
        ParseResource parseResource = new ParseResource();
        parseResource.setResourceDesc(resourceDesc);
        parseResource.setRemark(remark);
        parseResource.setLocation(upload);

        return toAjax(parseResourceService.insertParseResource(parseResource, upload));
    }

    /**
     * 解析资源
     */
    @Log(title = "资源", businessType = BusinessType.OTHER)
    @PostMapping("/parse/{resourceId}")
    @ResponseBody
    public AjaxResult parse(@PathVariable("resourceId") Long resourceId) {
        return toAjax(parseResourceService.parseResource(resourceId));
    }

    /**
     * 修改资源
     */
    @GetMapping("/edit/{resourceId}")
    public String edit(@PathVariable("resourceId") Long resourceId, ModelMap mmap) {
        ParseResource parseResource = parseResourceService.selectParseResourceByResourceId(resourceId);
        mmap.put("parseResource", parseResource);
        return prefix + "/edit";
    }

    /**
     * 修改保存资源
     */
    @Log(title = "资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseResource parseResource) {
        return toAjax(parseResourceService.updateParseResource(parseResource));
    }

    /**
     * 删除资源
     */
    @Log(title = "资源", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(parseResourceService.deleteParseResourceByResourceIds(ids));
    }


}
