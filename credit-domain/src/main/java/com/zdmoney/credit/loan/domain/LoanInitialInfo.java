package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanInitialInfo extends BaseDomain {
	private static final long serialVersionUID = -4709631267543945809L;

	private Long id;

    private Long assessorId;

    private String loanType;

    private BigDecimal money;

    private String purpose;

    private Date requestDate;

    private BigDecimal restoreem;

    private BigDecimal auditingMoney;

    private BigDecimal requestMoney;

    private Date grantMoneyDate;

    private Long grantMoneyOperatorId;

    private Date signDate;

    private String requestLoanType;

    private Long requestTime;

    private Date auditDate;

    private BigDecimal approveMoney;

    private Long approveTime;

    private Date createTime;

    private Date updateTime;

    private Long loanId;

    private Long lastAssessorId;

    private Date grantMoneyDateTtp;

    private String appNo;
    
    private Date contractDate;
    
    private String isRatePreferLoan;
    
    private String directorCode;

	private String channelPushNo;

	public String getDirectorCode() {
		return directorCode;
	}

	public void setDirectorCode(String directorCode) {
		this.directorCode = directorCode;
	}

	public String getIsRatePreferLoan() {
		return isRatePreferLoan;
	}

	public void setIsRatePreferLoan(String isRatePreferLoan) {
		this.isRatePreferLoan = isRatePreferLoan;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssessorId() {
		return assessorId;
	}

	public void setAssessorId(Long assessorId) {
		this.assessorId = assessorId;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public BigDecimal getRestoreem() {
		return restoreem;
	}

	public void setRestoreem(BigDecimal restoreem) {
		this.restoreem = restoreem;
	}

	public BigDecimal getAuditingMoney() {
		return auditingMoney;
	}

	public void setAuditingMoney(BigDecimal auditingMoney) {
		this.auditingMoney = auditingMoney;
	}

	public BigDecimal getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(BigDecimal requestMoney) {
		this.requestMoney = requestMoney;
	}

	public Date getGrantMoneyDate() {
		return grantMoneyDate;
	}

	public void setGrantMoneyDate(Date grantMoneyDate) {
		this.grantMoneyDate = grantMoneyDate;
	}

	public Long getGrantMoneyOperatorId() {
		return grantMoneyOperatorId;
	}

	public void setGrantMoneyOperatorId(Long grantMoneyOperatorId) {
		this.grantMoneyOperatorId = grantMoneyOperatorId;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getRequestLoanType() {
		return requestLoanType;
	}

	public void setRequestLoanType(String requestLoanType) {
		this.requestLoanType = requestLoanType;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public BigDecimal getApproveMoney() {
		return approveMoney;
	}

	public void setApproveMoney(BigDecimal approveMoney) {
		this.approveMoney = approveMoney;
	}

	public Long getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Long approveTime) {
		this.approveTime = approveTime;
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

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getLastAssessorId() {
		return lastAssessorId;
	}

	public void setLastAssessorId(Long lastAssessorId) {
		this.lastAssessorId = lastAssessorId;
	}

	public Date getGrantMoneyDateTtp() {
		return grantMoneyDateTtp;
	}

	public void setGrantMoneyDateTtp(Date grantMoneyDateTtp) {
		this.grantMoneyDateTtp = grantMoneyDateTtp;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public String getChannelPushNo() {
		return channelPushNo;
	}

	public void setChannelPushNo(String channelPushNo) {
		this.channelPushNo = channelPushNo;
	}
}