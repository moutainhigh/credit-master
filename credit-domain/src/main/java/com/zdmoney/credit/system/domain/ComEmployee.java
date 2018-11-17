package com.zdmoney.credit.system.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 员工实体
 * @author Ivan
 *
 */
public class ComEmployee extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8575274973143852673L;

	private Long id;

    private Long orgId;

    private String inActive;

    private String name;

    private String password;

    private String usercode;

    private String acceptAuditTask;

    private String dempLevel;

    private String memo;

    private Date createTime;

    private Date updateTime;

    private String updator;

    private String employeeType;
    
    private String email;
    
    private String fullName;
    private Date leaveOfficeDate;
    private String mobile;
    private String isFirst;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getInActive() {
        return inActive;
    }

    public void setInActive(String inActive) {
        this.inActive = inActive == null ? null : inActive.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public String getAcceptAuditTask() {
        return acceptAuditTask;
    }

    public void setAcceptAuditTask(String acceptAuditTask) {
        this.acceptAuditTask = acceptAuditTask == null ? null : acceptAuditTask.trim();
    }

    public String getDempLevel() {
        return dempLevel;
    }

    public void setDempLevel(String dempLevel) {
        this.dempLevel = dempLevel == null ? null : dempLevel.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getLeaveOfficeDate() {
		return leaveOfficeDate;
	}

	public void setLeaveOfficeDate(Date leaveOfficeDate) {
		this.leaveOfficeDate = leaveOfficeDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	
	
}