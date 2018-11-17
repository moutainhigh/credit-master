package com.zdmoney.credit.operation.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanContractCloseBusinessInfo extends BaseDomain {
    private Long id;

    private Long orgId;

    private Long type;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private Long editType;

    private Date activityTime;

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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getEditType() {
        return editType;
    }

    public void setEditType(Long editType) {
        this.editType = editType;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
}