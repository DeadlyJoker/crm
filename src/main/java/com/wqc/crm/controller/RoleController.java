package com.wqc.crm.controller;

import com.wqc.crm.base.BaseController;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.query.RoleQuery;
import com.wqc.crm.service.RoleService;
import com.wqc.crm.vo.Role;
import com.wqc.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/14
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> getAllRoles(RoleQuery query){
        return roleService.getList(query);
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> getAllRoles(Integer id){
        return roleService.queryAllRoles(id);
    }

    @RequestMapping("index")
    public String toIndex(){
        return "role/role";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success();
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRoles(Integer[] ids){
        roleService.deleteRoles(ids);
        return success();
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success();
    }

    @RequestMapping("toAddOrUpdatePage")
    public String toAddOrUpdatePage(Integer id, HttpServletRequest request){
        if (id != null){
            Role role = roleService.selectByPrimaryKey(id);
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        if (roleId != null){
            request.setAttribute("roleId",roleId);
        }
        return "role/grant";
    }

}
