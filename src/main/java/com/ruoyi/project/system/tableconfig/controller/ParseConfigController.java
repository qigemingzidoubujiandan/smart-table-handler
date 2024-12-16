package com.ruoyi.project.system.tableconfig.controller;

import java.util.List;

import com.ruoyi.project.system.file.domain.ParseResourceFile;
import com.ruoyi.project.system.resource.domain.ParseResource;
import com.ruoyi.project.system.resource.service.IParseResourceService;
import com.ruoyi.project.system.tableconfig.domain.ParseConfigDTO;
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
import com.ruoyi.project.system.tableconfig.domain.ParseConfig;
import com.ruoyi.project.system.tableconfig.service.IParseConfigService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 解析配置Controller
 *
 * @author zz
 * @date 2024-12-10
 */
@Controller
@RequestMapping("/system/tableconfig")
public class ParseConfigController extends BaseController {
    private String prefix = "system/tableconfig";

    @Autowired
    private IParseConfigService parseConfigService;

    @Autowired
    private IParseResourceService parseResourceService;

    @RequiresPermissions("system:tableconfig:view")
    @GetMapping()
    public String tableconfig(ModelMap mmap) {
        mmap.put("resources", parseResourceService.selectParseResourceList(new ParseResource()));  //这里key是需要待会和前端对应的，稍后会备注，value就是我查询到的一个结果
        return prefix + "/tableconfig";
    }

    /**
     * 查询解析配置列表
     */
    @RequiresPermissions("system:tableconfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseConfig parseConfig) {
        startPage();
        List<ParseConfigDTO> list = parseConfigService.selectParseConfigList(parseConfig);
        return getDataTable(list);
    }

    /**
     * 导出解析配置列表
     */
    @RequiresPermissions("system:tableconfig:export")
    @Log(title = "解析配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseConfig parseConfig) {
        List<ParseConfig> list = parseConfigService.selectList(parseConfig);
        ExcelUtil<ParseConfig> util = new ExcelUtil<ParseConfig>(ParseConfig.class);
        return util.exportExcel(list, "解析配置数据");
    }

    /**
     * 新增解析配置
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("resources", parseResourceService.selectParseResourceList(new ParseResource()));  //这里key是需要待会和前端对应的，稍后会备注，value就是我查询到的一个结果
        return prefix + "/add";
    }

    /**
     * 新增保存解析配置
     */
    @RequiresPermissions("system:tableconfig:add")
    @Log(title = "解析配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ParseConfig parseConfig) {
        return toAjax(parseConfigService.insertParseConfig(parseConfig));
    }

    /**
     * 修改解析配置
     */
    @RequiresPermissions("system:tableconfig:edit")
    @GetMapping("/edit/{parseConfigId}")
    public String edit(@PathVariable("parseConfigId") Long parseConfigId, ModelMap mmap) {
        ParseConfig parseConfig = parseConfigService.selectParseConfigByParseConfigId(parseConfigId);
        mmap.put("parseConfig", parseConfig);

        mmap.put("resources", parseResourceService.selectParseResourceList(new ParseResource()));  //这里key是需要待会和前端对应的，稍后会备注，value就是我查询到的一个结果
        mmap.put("defaultResourceId", parseConfig.getResourceId());
        return prefix + "/edit";
    }

    /**
     * 修改保存解析配置
     */
    @RequiresPermissions("system:tableconfig:edit")
    @Log(title = "解析配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseConfig parseConfig, ModelMap mmap) {
        return toAjax(parseConfigService.updateParseConfig(parseConfig));
    }

    /**
     * 删除解析配置
     */
    @RequiresPermissions("system:tableconfig:remove")
    @Log(title = "解析配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(parseConfigService.deleteParseConfigByParseConfigIds(ids));
    }

}
