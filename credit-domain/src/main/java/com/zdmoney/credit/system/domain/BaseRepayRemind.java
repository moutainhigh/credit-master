package com.zdmoney.credit.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BaseRepayRemind extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2106314509018236329L;

	private Long id;

    private BigDecimal amount;

    private String deliverState;

    private String deliverStateRemark;

    private Long loanId;

    private String loanState;

    private String mphone;

    private Date repayDate;

    private String sendState;

    private String sendStateRemark;

    private String type;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState == null ? null : loanState.trim();
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone == null ? null : mphone.trim();
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}