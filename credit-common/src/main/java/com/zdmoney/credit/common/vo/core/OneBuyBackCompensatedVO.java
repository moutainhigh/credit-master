package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

public class OneBuyBackCompensatedVO {
	
	private Long loanId; // 借款ID
	private String appNo;
	private BigDecimal totalAmount ; 	
	private BigDecimal capital ;// 本金221
	private BigDecimal aint ;//利息
	private BigDecimal oint ;//罚息
	private BigDecimal penalty;//违约金
	private Date tradeDate;
	private String type;
	private Long term;
	private String channelPushNo; // 最终申请单号
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getCapital() {
		return capital;
	}
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}
	public BigDecimal getAint() {
		return aint;
	}
	public void setAint(BigDecimal aint) {
		this.aint = aint;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getOint() {
		return oint;
	}
	public void setOint(BigDecimal oint) {
		this.oint = oint;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public Long getTerm() {
		return term;
	}
	public void setTerm(Long term) {
		this.term = term;
	}
	public String getChannelPushNo() {
		return channelPushNo;
	}
	public void setChannelPushNo(String channelPushNo) {
		this.channelPushNo = channelPushNo;
	}
}
