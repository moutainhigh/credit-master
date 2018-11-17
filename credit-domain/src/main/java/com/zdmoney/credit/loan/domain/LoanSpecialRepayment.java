package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 特殊还款表
 * 
 * @author 00232949
 *
 */
public class LoanSpecialRepayment extends BaseDomain {
    /**
	 * 
	 */
    private static final long serialVersionUID = 4686967001572345145L;

    private Long id;

    private Long loanId;

    private String memo;

    private Long proposerId;

    private Date requestDate;

    private String specialRepaymentType;

    private String specialRepaymentState;

    private BigDecimal amount;

    private Date createTime;

    private Date updateTime;
    
    private Date closingDate;

    public String getMemo() {
	return memo;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getLoanId() {
	return loanId;
    }

    public void setLoanId(Long loanId) {
	this.loanId = loanId;
    }

    public Long getProposerId() {
	return proposerId;
    }

    public void setProposerId(Long proposerId) {
	this.proposerId = proposerId;
    }

    public void setMemo(String memo) {
	this.memo = memo == null ? null : memo.trim();
    }

    public Date getRequestDate() {
	return requestDate;
    }

    public void setRequestDate(Date requestDate) {
	if(requestDate!=null){
		this.requestDate = Dates.parse(Dates.getDateTime(requestDate, Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
	}
    }

    public String getSpecialRepaymentType() {
	return specialRepaymentType;
    }

    public void setSpecialRepaymentType(String specialRepaymentType) {
	this.specialRepaymentType = specialRepaymentType == null ? null : specialRepaymentType.trim();
    }

    public String getSpecialRepaymentState() {
	return specialRepaymentState;
    }

    public void setSpecialRepaymentState(String specialRepaymentState) {
	this.specialRepaymentState = specialRepaymentState == null ? null : specialRepaymentState.trim();
    }

    public BigDecimal getAmount() {
	return amount;
    }

    public void setAmount(BigDecimal amount) {
	this.amount = amount;
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

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }
    
}