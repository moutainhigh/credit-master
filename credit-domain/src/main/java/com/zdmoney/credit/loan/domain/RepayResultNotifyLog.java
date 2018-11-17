package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class RepayResultNotifyLog extends BaseDomain {
	
	private static final long serialVersionUID = 8492096519774895856L;
	private Long id;
	private Long loanId;
	private	String bankCardNo; 
	private	String currency;
	private	String orderNo;
	private Integer period;
	private	String scheduleDate;
	private	String payResult;
	private	String failReason;
	private	BigDecimal totalamt;
	private	String state ;
	private String dealState ;
	private	String repayBusNumber;
	private	String  respcd;
	private	String  resptx;
	private String notifyType;
	private String deductState;
	private	Date  updateTime ;
	private	Date  createTime;
	
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
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public BigDecimal getTotalamt() {
		return totalamt;
	}
	public void setTotalamt(BigDecimal totalamt) {
		this.totalamt = totalamt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDealState() {
        return dealState;
    }
    public void setDealState(String dealState) {
        this.dealState = dealState;
    }
    public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getRepayBusNumber() {
		return repayBusNumber;
	}
	public void setRepayBusNumber(String repayBusNumber) {
		this.repayBusNumber = repayBusNumber;
	}
	public String getRespcd() {
		return respcd;
	}
	public void setRespcd(String respcd) {
		this.respcd = respcd;
	}
	public String getResptx() {
		return resptx;
	}
	public void setResptx(String resptx) {
		this.resptx = resptx;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getDeductState() {
		return deductState;
	}
	public void setDeductState(String deductState) {
		this.deductState = deductState;
	}
	
}
