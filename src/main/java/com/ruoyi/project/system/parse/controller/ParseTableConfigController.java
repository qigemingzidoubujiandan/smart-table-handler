package com.ruoyi.project.system.parse.controller;

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
import com.ruoyi.project.system.parse.domain.ParseTableConfig;
import com.ruoyi.project.system.parse.service.IParseTableConfigService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 格配置Controller
 * 
 * @author zhaochenliang
 * @date 2024-12-06
 */
@Controller
@RequestMapping("/parse/parse")
public class ParseTableConfigController extends BaseController
{
    private String prefix = "parse/parse";

    @Autowired
    private IParseTableConfigService parseTableConfigService;

    @RequiresPermissions("parse:parse:view")
    @GetMapping()
    public String parse()
    {
        return prefix + "/parse";
    }

    /**
     * 查询格配置列表
     */
    @RequiresPermissions("parse:parse:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ParseTableConfig parseTableConfig)
    {
        startPage();
        List<ParseTableConfig> list = parseTableConfigService.selectParseTableConfigList(parseTableConfig);
        return getDataTable(list);
    }

    /**
     * 导出格配置列表
     */
    @RequiresPermissions("parse:parse:export")
    @Log(title = "格配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ParseTableConfig parseTableConfig)
    {
        List<ParseTableConfig> list = parseTableConfigService.selectParseTableConfigList(parseTableConfig);
        ExcelUtil<ParseTableConfig> util = new ExcelUtil<ParseTableConfig>(ParseTableConfig.class);
        return util.exportExcel(list, "格配置数据");
    }

    /**
     * 新增格配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存格配置
     */
    @RequiresPermissions("parse:parse:add")
    @Log(title = "格配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ParseTableConfig parseTableConfig)
    {
        return toAjax(parseTableConfigService.insertParseTableConfig(parseTableConfig));
    }

    /**
     * 修改格配置
     */
    @RequiresPermissions("parse:parse:edit")
    @GetMapping("/edit/{tableConfigId}")
    public String edit(@PathVariable("tableConfigId") Long tableConfigId, ModelMap mmap)
    {
        ParseTableConfig parseTableConfig = parseTableConfigService.selectParseTableConfigByTableConfigId(tableConfigId);
        mmap.put("parseTableConfig", parseTableConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存格配置
     */
    @RequiresPermissions("parse:parse:edit")
    @Log(title = "格配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ParseTableConfig parseTableConfig)
    {
        return toAjax(parseTableConfigService.updateParseTableConfig(parseTableConfig));
    }

    /**
     * 删除格配置
     */
    @RequiresPermissions("parse:parse:remove")
    @Log(title = "格配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(parseTableConfigService.deleteParseTableConfigByTableConfigIds(ids));
    }
}
