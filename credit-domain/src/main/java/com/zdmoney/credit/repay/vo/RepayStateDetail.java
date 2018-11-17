package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;
import java.util.Date;

public class RepayStateDetail {
	private	String projectCode;//信托项目简码
	private	String contractNum;//合同编号/借款编号
	private Long term;//期次
	private	String returnDate;//应还款日期
	private	String lastDay;//宽限期最后一天
	private	BigDecimal returnCorpus;//应还本金
	private	BigDecimal returnAccrual;//应还利息
	private	BigDecimal returnCorpusOine;//应还本金罚息
	private	BigDecimal returnAccrualOine;//应还利息罚息
	private String realReturnDate;//实际还款日期
	private	BigDecimal realCorpus;//实还本金
	private	BigDecimal realAccrual;//实还利息
	private	BigDecimal realCorpusOine;//实还本金罚息
	private	BigDecimal realAccrualOine;//实还利息罚息
	private	String  currentPayOffFlag;//本期结清标志
	private	String  update;//更新日期
	private	String  accountState;//账户状态
	private	String  payOffDate;//结清日期
	private	BigDecimal overdueCorpus;//逾期本金
	private	BigDecimal overdueAccrual;//逾期利息
	private Integer overdueDays;//逾期天数
	private BigDecimal tradeAmount;//总金额
	private Date overdueDate;
	
	public Date getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}
	public Integer getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public Long getTerm() {
		return term;
	}
	public void setTerm(Long term) {
		this.term = term;
	}
	
	public String getLastDay() {
		return lastDay;
	}
	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}
	public BigDecimal getReturnCorpus() {
		return returnCorpus;
	}
	public void setReturnCorpus(BigDecimal returnCorpus) {
		this.returnCorpus = returnCorpus;
	}
	public BigDecimal getReturnAccrual() {
		return returnAccrual;
	}
	public void setReturnAccrual(BigDecimal returnAccrual) {
		this.returnAccrual = returnAccrual;
	}
	public BigDecimal getReturnCorpusOine() {
		return returnCorpusOine;
	}
	public void setReturnCorpusOine(BigDecimal returnCorpusOine) {
		this.returnCorpusOine = returnCorpusOine;
	}
	public BigDecimal getReturnAccrualOine() {
		return returnAccrualOine;
	}
	public void setReturnAccrualOine(BigDecimal returnAccrualOine) {
		this.returnAccrualOine = returnAccrualOine;
	}
	public BigDecimal getRealCorpus() {
		return realCorpus;
	}
	public void setRealCorpus(BigDecimal realCorpus) {
		this.realCorpus = realCorpus;
	}
	public BigDecimal getRealAccrual() {
		return realAccrual;
	}
	public void setRealAccrual(BigDecimal realAccrual) {
		this.realAccrual = realAccrual;
	}
	public BigDecimal getRealCorpusOine() {
		return realCorpusOine;
	}
	public void setRealCorpusOine(BigDecimal realCorpusOine) {
		this.realCorpusOine = realCorpusOine;
	}
	public BigDecimal getRealAccrualOine() {
		return realAccrualOine;
	}
	public void setRealAccrualOine(BigDecimal realAccrualOine) {
		this.realAccrualOine = realAccrualOine;
	}
	public String getAccountState() {
		return accountState;
	}
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	
	public BigDecimal getOverdueCorpus() {
		return overdueCorpus;
	}
	public void setOverdueCorpus(BigDecimal overdueCorpus) {
		this.overdueCorpus = overdueCorpus;
	}
	public BigDecimal getOverdueAccrual() {
		return overdueAccrual;
	}
	public void setOverdueAccrual(BigDecimal overdueAccrual) {
		this.overdueAccrual = overdueAccrual;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getRealReturnDate() {
		return realReturnDate;
	}
	public void setRealReturnDate(String realReturnDate) {
		this.realReturnDate = realReturnDate;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getCurrentPayOffFlag() {
		return currentPayOffFlag;
	}
	public void setCurrentPayOffFlag(String currentPayOffFlag) {
		this.currentPayOffFlag = currentPayOffFlag;
	}
	public String getPayOffDate() {
		return payOffDate;
	}
	public void setPayOffDate(String payOffDate) {
		this.payOffDate = payOffDate;
	}
	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	
}