package com.zdmoney.credit.payment.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ThirdLineOfferTransaction extends BaseDomain {

    private static final long serialVersionUID = -8010611323390367493L;

    /** 主键Id **/
    private Long id;

    /** 备注 **/
    private String remark;

    /** 反馈码 **/
    private String feedbackCode;

    /** 原因 **/
    private String reason;

    /** 报盘Id**/
    private Long twoOfferId;

    /** 流水号 **/
    private String flowNumber;

    /** 报盘批次Id**/
    private Long batchId;

    /** 记录序号 **/
    private String recordNumber;

    /** 报盘状态（未报盘 已报盘 扣款成功 扣款失败）**/
    private String state;

    /** 回盘时间 **/
    private Date returnTime;

    /** 发送时间 **/
    private Date sendTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getFeedbackCode() {
        return feedbackCode;
    }

    public void setFeedbackCode(String feedbackCode) {
        this.feedbackCode = feedbackCode == null ? null : feedbackCode.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Long getTwoOfferId() {
        return twoOfferId;
    }

    public void setTwoOfferId(Long twoOfferId) {
        this.twoOfferId = twoOfferId;
    }

    public String getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(String flowNumber) {
        this.flowNumber = flowNumber == null ? null : flowNumber.trim();
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber == null ? null : recordNumber.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}