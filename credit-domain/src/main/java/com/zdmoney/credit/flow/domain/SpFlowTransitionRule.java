package com.zdmoney.credit.flow.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.util.Date;

/**
 * 审批流程流转规则--配置一个流程有哪些环节节点组成
 */
public class SpFlowTransitionRule extends BaseDomain {
    private static final long serialVersionUID = 5843954211021103982L;

    private Long id;

    private Long flowId;

    private Long startNodeId;

    private Long stopNodeId;

    private String transitionCondition;

    private String status;

    private String isAuto;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private String stopNodeName;

    private String startNodeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(Long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public Long getStopNodeId() {
        return stopNodeId;
    }

    public void setStopNodeId(Long stopNodeId) {
        this.stopNodeId = stopNodeId;
    }

    public String getTransitionCondition() {
        return transitionCondition;
    }

    public void setTransitionCondition(String transitionCondition) {
        this.transitionCondition = transitionCondition == null ? null : transitionCondition.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto == null ? null : isAuto.trim();
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public String getStopNodeName() {
        return stopNodeName;
    }

    public void setStopNodeName(String stopNodeName) {
        this.stopNodeName = stopNodeName;
    }

    public String getStartNodeName() {
        return startNodeName;
    }

    public void setStartNodeName(String startNodeName) {
        this.startNodeName = startNodeName;
    }
}