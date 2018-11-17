package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;
import java.util.Date;


public class RepayDetail {
	private Long currentTerm;
	private BigDecimal currentAccrual;
	private BigDecimal currCorpus;	
	private	BigDecimal returneterm;
	private	String returnDate;
	private BigDecimal repaymentAll;
	private	BigDecimal principalBalance;
	private	BigDecimal penalty;
	private	BigDecimal giveBackRate;
	
	public Long getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}
	public BigDecimal getCurrentAccrual() {
		return currentAccrual;
	}
	public void setCurrentAccrual(BigDecimal currentAccrual) {
		this.currentAccrual = currentAccrual;
	}
	public BigDecimal getCurrCorpus() {
		return currCorpus;
	}
	public void setCurrCorpus(BigDecimal currCorpus) {
		this.currCorpus = currCorpus;
	}
	public BigDecimal getReturneterm() {
		return returneterm;
	}
	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public BigDecimal getRepaymentAll() {
		return repaymentAll;
	}
	public void setRepaymentAll(BigDecimal repaymentAll) {
		this.repaymentAll = repaymentAll;
	}
	public BigDecimal getPrincipalBalance() {
		return principalBalance;
	}
	public void setPrincipalBalance(BigDecimal principalBalance) {
		this.principalBalance = principalBalance;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getGiveBackRate() {
		return giveBackRate;
	}
	public void setGiveBackRate(BigDecimal giveBackRate) {
		this.giveBackRate = giveBackRate;
	}
}