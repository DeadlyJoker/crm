package com.wqc.crm.dao;

import com.wqc.crm.base.BaseMapper;
import com.wqc.crm.model.TreeModel;
import com.wqc.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    /**
     * 查询所有的模块
     * @return
     */
    List<TreeModel> queryAllModules();

    List<Integer> selectPermissionByRoleId(Integer rId);

    /**
     * 查询模块
     * @return
     */
    List<Module> queryModules();

    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

    Module queryModuleByOptValue(String optValue);

    List<Map<String, Object>> queryAllModulesByGrade(Integer grade);
}