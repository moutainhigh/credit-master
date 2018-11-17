package com.zdmoney.credit.system.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ComRoleHierarchy extends BaseDomain{
    private Long id;

    private Long roleId;

    private Long sonRoleId;

    private String sonRoleName;

    private Date createTime;

    private String roleName;

    private String creator;

    private String sp1;

    private String sp2;

    private String sp3;

    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getSonRoleId() {
		return sonRoleId;
	}

	public void setSonRoleId(Long sonRoleId) {
		this.sonRoleId = sonRoleId;
	}

	public String getSonRoleName() {
        return sonRoleName;
    }

    public void setSonRoleName(String sonRoleName) {
        this.sonRoleName = sonRoleName == null ? null : sonRoleName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getSp1() {
        return sp1;
    }

    public void setSp1(String sp1) {
        this.sp1 = sp1 == null ? null : sp1.trim();
    }

    public String getSp2() {
        return sp2;
    }

    public void setSp2(String sp2) {
        this.sp2 = sp2 == null ? null : sp2.trim();
    }

    public String getSp3() {
        return sp3;
    }

    public void setSp3(String sp3) {
        this.sp3 = sp3 == null ? null : sp3.trim();
    }
}