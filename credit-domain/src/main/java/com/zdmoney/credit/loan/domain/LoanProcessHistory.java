package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanProcessHistory extends BaseDomain{
	private static final long serialVersionUID = 3770868599306295646L;
    private Long id;

    private String content;

    private Long loanId;

    private String loanFlowState;

    private String creator;

    private String loanState;

    private String privateContent;

    private Long rejectReasonId;

    private Long sendbackReasonId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanFlowState() {
        return loanFlowState;
    }

    public void setLoanFlowState(String loanFlowState) {
        this.loanFlowState = loanFlowState == null ? null : loanFlowState.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState == null ? null : loanState.trim();
    }

    public String getPrivateContent() {
        return privateContent;
    }

    public void setPrivateContent(String privateContent) {
        this.privateContent = privateContent == null ? null : privateContent.trim();
    }

    public Long getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(Long rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public Long getSendbackReasonId() {
        return sendbackReasonId;
    }

    public void setSendbackReasonId(Long sendbackReasonId) {
        this.sendbackReasonId = sendbackReasonId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}