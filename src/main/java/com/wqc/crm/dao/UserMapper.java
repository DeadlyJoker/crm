package com.wqc.crm.dao;

import com.wqc.crm.base.BaseMapper;
import com.wqc.crm.vo.Role;
import com.wqc.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    public User selectUserByName(String userName);

    List<Map<String,Object>> selectAllSaleRole();

    List<Map<String,Object>> queryAllRoles(Integer userId);
}