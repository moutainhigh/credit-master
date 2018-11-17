package com.zdmoney.credit.system.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SysOperationLog extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2259162663598284309L;

	private Long id;

    private Long employeeId;

    private String token;

    private Integer operationType;

    private String operateIp;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }

}