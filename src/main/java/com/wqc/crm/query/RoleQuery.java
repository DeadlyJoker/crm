package com.wqc.crm.query;

import com.wqc.crm.base.BaseQuery;

/**
 * Created by WQC on 2021/1/14
 */
public class RoleQuery extends BaseQuery {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
