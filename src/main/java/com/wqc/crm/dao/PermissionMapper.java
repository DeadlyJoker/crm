package com.wqc.crm.dao;

import com.wqc.crm.base.BaseMapper;
import com.wqc.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    Integer countPermission(Integer roleId);

    Integer deletePermissionByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);
}