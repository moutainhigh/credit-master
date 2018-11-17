package com.zdmoney.credit.flow.vo;

import java.io.Serializable;

/**
 * Created by ym10094 on 2017/9/6.
 */
public class EmployeeRoleVo implements Serializable {

    private static final long serialVersionUID = -7981182924862263813L;

    private Long employeeId;

    private String roleName;

    private Long roleId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
