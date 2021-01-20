package com.wqc.crm.controller;

import com.wqc.crm.base.BaseController;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.model.TreeModel;
import com.wqc.crm.service.ModuleService;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/15
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer rId) {
        return moduleService.queryAllModules(rId);
    }

    @RequestMapping("index")
    public String index() {
        return "module/module";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> moduleList() {
        return moduleService.moduleList();
    }

    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade, Integer parentId, Model model) {
        model.addAttribute("grade", grade);
        model.addAttribute("parentId", parentId);
        return "module/add";
    }

    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model) {
        model.addAttribute("module", moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveModule(Module module) {
        moduleService.saveModule(module);
        return success("菜单添加成功");
    }

    @RequestMapping("queryAllModulesByGrade")
    @ResponseBody
    public List<Map<String, Object>> queryAllModulesByGrade(Integer grade) {
        return moduleService.queryAllModulesByGrade(grade);
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module) {
        moduleService.updateModule(module);
        return success("菜单更新成功");
    }
}

