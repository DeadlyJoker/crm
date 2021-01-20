package com.wqc.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wqc.crm.base.BaseService;
import com.wqc.crm.dao.ModuleMapper;
import com.wqc.crm.dao.PermissionMapper;
import com.wqc.crm.dao.RoleMapper;
import com.wqc.crm.dao.UserRoleMapper;
import com.wqc.crm.query.RoleQuery;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.vo.Permission;
import com.wqc.crm.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by WQC on 2021/1/14
 */

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    public Map<String,Object> getList(RoleQuery query){
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<Role> roles = roleMapper.selectByParams(query);
        PageInfo<Role> pageInfo = PageInfo.of(roles);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    public List<Map<String,Object>> queryAllRoles(Integer id){
        return roleMapper.queryAllRoles(id);
    }

    @Transactional
    public void addRole(Role role) {
        checkRoleData(role);
        Role dbRole = roleMapper.selectByName(role.getRoleName());
        AssertUtil.isTrue(dbRole != null,"角色名已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insert(role) < 1,"角色添加失败");
    }

    @Transactional
    public void deleteRoles(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0 ,"请选择要删除的数据");
        AssertUtil.isTrue(roleMapper.deleteBatch(ids) < 1,"角色删除失败");
        Integer count = userRoleMapper.selectByRoleId(ids);
        if(count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByRoleIds(ids) < count,"用户角色关联删除失败");
        }
    }

    private void checkRoleData(Role role){
        AssertUtil.isTrue(role.getRoleName() == null,"角色名称不能为空");
        AssertUtil.isTrue(role.getRoleRemark() == null,"角色备注不能为空");
    }

    @Transactional
    public void updateRole(Role role) {
        AssertUtil.isTrue(role.getId() == null ||  roleMapper.selectByPrimaryKey(role.getId()) ==null,"数据异常，请重试");
        AssertUtil.isTrue(roleMapper.selectByName(role.getRoleName()) != null,"角色已存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"修改失败");
    }

    @Transactional
    public void addGrant(Integer roleId, Integer[] mIds) {
        AssertUtil.isTrue(roleId == null ,"数据异常请重试");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null,"角色不存在");
        Integer count = permissionMapper.countPermission(roleId);
        if(count > 0){
            //将原有的资源全部删除
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId) != count,"数据异常请重试");
        }
        //给角色绑定权限
        List<Permission> permissions = new ArrayList<>();
        if (mIds.length > 0){

            for(Integer mid:mIds){
                Permission permission = new Permission();

                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());//设置权限码  需要去module表中查询得到
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());

                permissions.add(permission);
            }

            //执行批量添加操作，绑定多个权限
            AssertUtil.isTrue(permissionMapper.insertBatch(permissions) != permissions.size(),"权限绑定失败");
        }
    }
}
