package com.wqc.crm.service;

import com.wqc.crm.base.BaseService;
import com.wqc.crm.dao.ModuleMapper;
import com.wqc.crm.model.TreeModel;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/15
 */
@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    public List<TreeModel> queryAllModules(Integer rId) {
        List<TreeModel> treeModels = moduleMapper.queryAllModules();

        List<Integer> mIds = moduleMapper.selectPermissionByRoleId(rId);
        if (mIds != null && mIds.size() > 0) {
            treeModels.forEach(model -> {
                if (mIds.contains(model.getId())) {
                    model.setChecked(true);
                    model.setOpen(true);
                }
            });
        }
        return treeModels;
    }

    public Map<String, Object> moduleList() {
        Map<String, Object> result = new HashMap<>();
        List<Module> modules = moduleMapper.queryModules();
        result.put("count", modules.size());
        result.put("data", modules);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module) {
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "请输入菜单名!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合法!");
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()), "该层级下菜单重复!");
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "请指定二级菜单url值");
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl()), "二级菜单url不可重复!");
        }
        if (grade != 0) {
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null == parentId || null == selectByPrimaryKey(parentId), "请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "请输入权限码!");
        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptValue(module.getOptValue()), "权限码重复!");
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(module) < 1, "菜单添加失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        AssertUtil.isTrue(null == module.getId() || null == selectByPrimaryKey(module.getId()), "待更新记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "请 指定菜单名称!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合法!");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()); if (null != temp) {
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "该层级下菜单已存在!");
        }
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "请指定二级菜单url值");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl());
            if (null != temp) {
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "该层级下url已存在!");
            }
        }
        if (grade != 0) {
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null == parentId || null == selectByPrimaryKey(parentId), "请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "请输 入权限码!");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if (null != temp) {
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "权限 码已存在!");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(module) < 1, "菜单更新失 败!");
    }

    public List<Map<String, Object>> queryAllModulesByGrade(Integer grade) {
        return moduleMapper.queryAllModulesByGrade(grade);
    }
}
