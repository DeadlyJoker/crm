package com.wqc.crm.dao;

import com.wqc.crm.base.BaseMapper;
import com.wqc.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    Integer countUserRoleByUserId(Integer id);

    Integer deleteUserRoleByUserId(Integer id);

    Integer getMaxId();

    Integer deleteUserRoleByRoleIds(Integer[] ids);

    Integer selectByRoleId(Integer[] ids);
}