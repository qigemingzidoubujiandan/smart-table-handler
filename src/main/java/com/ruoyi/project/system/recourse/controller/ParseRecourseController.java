package com.ruoyi.project.system.recourse.controller;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.recourse.domain.ParseRecourse;
import com.ruoyi.project.system.recourse.service.IParseRecourseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源Controller
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Controller
@RequestMapping("/system/recourse")
public class ParseRecourseController extends BaseController {
    private String prefix = "system/recourse";

    @Autowired
    private IParseRecourseService parseRecourseService;
    @Autowired
    private ServerConfig serverConfig;

    @GetMapping()
    public String recourse() {
        return prefix + "/recourse";
    }

    /**
     * 查询资源列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseRecourse parseRecourse) {
        startPage();
        List<ParseRecourse> list = parseRecourseService.selectParseRecourseList(parseRecourse);
        return getDataTable(list);
    }

    /**
     * 导出资源列表
     */
    @Log(title = "资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseRecourse parseRecourse) {
        List<ParseRecourse> list = parseRecourseService.selectParseRecourseList(parseRecourse);
        ExcelUtil<ParseRecourse> util = new ExcelUtil<ParseRecourse>(ParseRecourse.class);
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
                              @RequestParam("remark") String remark) throws IOException
    {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String upload = FileUploadUtils.upload(filePath, file);
        upload = upload.replaceAll("/profile", "");
        // 构建资源对象并保存到数据库
        ParseRecourse parseRecourse = new ParseRecourse();
        parseRecourse.setResourceDesc(resourceDesc);
        parseRecourse.setRemark(remark);
        parseRecourse.setLocation(upload);

        return toAjax(parseRecourseService.insertParseRecourse(parseRecourse, upload));
    }

    /**
     * 解析资源
     */
    @Log(title = "资源", businessType = BusinessType.OTHER)
    @PostMapping("/parse/{resourceId}")
    @ResponseBody
    public AjaxResult parse(@PathVariable("resourceId")Long resourceId) {
        return toAjax(parseRecourseService.parseResource(resourceId));
    }
    /**
     * 修改资源
     */
    @GetMapping("/edit/{resourceId}")
    public String edit(@PathVariable("resourceId") Long resourceId, ModelMap mmap) {
        ParseRecourse parseRecourse = parseRecourseService.selectParseRecourseByResourceId(resourceId);
        mmap.put("parseRecourse", parseRecourse);
        return prefix + "/edit";
    }

    /**
     * 修改保存资源
     */
    @Log(title = "资源", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseRecourse parseRecourse) {
        return toAjax(parseRecourseService.updateParseRecourse(parseRecourse));
    }

    /**
     * 删除资源
     */
    @Log(title = "资源", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(parseRecourseService.deleteParseRecourseByResourceIds(ids));
    }
}
