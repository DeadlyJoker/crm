package com.wqc.crm.dao;

import com.wqc.crm.base.BaseMapper;
import com.wqc.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    List<Map<String,Object>> queryAllRoles(Integer id);

    Role selectByName(String roleName);
}