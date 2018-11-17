package com.zdmoney.credit.system.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BaseEmssendInfo extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4002771540462215351L;

	private Long id;

    private String deliverState;

    private String deliverStateRemark;

    private String mobile;

    private Long repayRemindId;

    private String sendId;

    private String sendState;

    private String sendStateRemark;

    private String emsSendInfosIdx;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliverState() {
        return deliverState;
    }

    public void setDeliverState(String deliverState) {
        this.deliverState = deliverState == null ? null : deliverState.trim();
    }

    public String getDeliverStateRemark() {
        return deliverStateRemark;
    }

    public void setDeliverStateRemark(String deliverStateRemark) {
        this.deliverStateRemark = deliverStateRemark == null ? null : deliverStateRemark.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getRepayRemindId() {
        return repayRemindId;
    }

    public void setRepayRemindId(Long repayRemindId) {
        this.repayRemindId = repayRemindId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId == null ? null : sendId.trim();
    }

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState == null ? null : sendState.trim();
    }

    public String getSendStateRemark() {
        return sendStateRemark;
    }

    public void setSendStateRemark(String sendStateRemark) {
        this.sendStateRemark = sendStateRemark == null ? null : sendStateRemark.trim();
    }

    public String getEmsSendInfosIdx() {
        return emsSendInfosIdx;
    }

    public void setEmsSendInfosIdx(String emsSendInfosIdx) {
        this.emsSendInfosIdx = emsSendInfosIdx == null ? null : emsSendInfosIdx.trim();
    }

}