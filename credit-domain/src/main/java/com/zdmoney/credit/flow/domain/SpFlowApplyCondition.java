package com.zdmoney.credit.flow.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 审批流程适应条件
 */
public class SpFlowApplyCondition extends BaseDomain {
    private static final long serialVersionUID = 480218517379312511L;;

    private Long id;

    private String conditionType;

    private String condition;

    private String conditionDesc;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String getUpdator() {
        return updator;
    }

    @Override
    public void setUpdator(String updator) {
        this.updator = updator;
    }
}