package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划接口返回值类型VO
 * 
 * @author 00235304
 * 
 */
public class RDetailVO {

	private Long loanId; 			// 借款ID
	private Integer currentTerm; 	// 当前期数
	private Date returnDate; 		// 还款日期
	private String repaymentState; 	// 当前还款状态
	private Date factReturnDate; 	// 结清日期
	private Date penaltyDate; 		// 罚息起算日期
	private BigDecimal currentAccrual = BigDecimal.ZERO; 	// 当期利息
	private BigDecimal giveBackRate = BigDecimal.ZERO; 		// 一次性还款退费
	private BigDecimal principalBalance = BigDecimal.ZERO; 	// 本金余额
	private BigDecimal repaymentAll = BigDecimal.ZERO; 		// 一次性还款金额
	private BigDecimal deficit = BigDecimal.ZERO; 			// 剩余欠款,用于记录不足额部分
	private BigDecimal returneterm = BigDecimal.ZERO; 		// 每期还款金额
	private BigDecimal penalty = BigDecimal.ZERO; 			// 违约金
	private BigDecimal accrualRevise = BigDecimal.ZERO; 	// 对应第三方的贴息或扣息 (积木盒子)

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Integer getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(Integer currentTerm) {
		this.currentTerm = currentTerm;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getRepaymentState() {
		return repaymentState;
	}

	public void setRepaymentState(String repaymentState) {
		this.repaymentState = repaymentState;
	}

	public Date getFactReturnDate() {
		return factReturnDate;
	}

	public void setFactReturnDate(Date factReturnDate) {
		this.factReturnDate = factReturnDate;
	}

	public Date getPenaltyDate() {
		return penaltyDate;
	}

	public void setPenaltyDate(Date penaltyDate) {
		this.penaltyDate = penaltyDate;
	}

	public BigDecimal getCurrentAccrual() {
		return currentAccrual;
	}

	public void setCurrentAccrual(BigDecimal currentAccrual) {
		this.currentAccrual = currentAccrual;
	}

	public BigDecimal getGiveBackRate() {
		return giveBackRate;
	}

	public void setGiveBackRate(BigDecimal giveBackRate) {
		this.giveBackRate = giveBackRate;
	}

	public BigDecimal getPrincipalBalance() {
		return principalBalance;
	}

	public void setPrincipalBalance(BigDecimal principalBalance) {
		this.principalBalance = principalBalance;
	}

	public BigDecimal getRepaymentAll() {
		return repaymentAll;
	}

	public void setRepaymentAll(BigDecimal repaymentAll) {
		this.repaymentAll = repaymentAll;
	}

	public BigDecimal getDeficit() {
		return deficit;
	}

	public void setDeficit(BigDecimal deficit) {
		this.deficit = deficit;
	}

	public BigDecimal getReturneterm() {
		return returneterm;
	}

	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}

	public BigDecimal getPenalty() {
		return penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	public BigDecimal getAccrualRevise() {
		return accrualRevise;
	}

	public void setAccrualRevise(BigDecimal accrualRevise) {
		this.accrualRevise = accrualRevise;
	}

}
