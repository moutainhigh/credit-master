package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装返回还款计划信息的参数
 * @author 00234770
 * @date 2015年8月28日 上午11:44:43 
 *
 */
public class ReturnRepaymentDetailVo {
	
	BigDecimal currentAccrual;          //当期利息
	Long currentTerm;                //当前期数
    BigDecimal giveBackRate;            //一次性还款退费
    Long loanId;                         //借款ID
    BigDecimal principalBalance;       //本金余额
    BigDecimal repaymentAll;            //一次性还款金额
    Date returnDate;                    //还款日期
    BigDecimal deficit;                  //剩余欠款,用于记录不足额部分
    String repaymentState;             //当前还款状态
    Date factReturnDate;               //结清日期
    Date penaltyDate;                  //罚息起算日期
    BigDecimal returneterm;            //每期还款金额
    BigDecimal penalty;                //违约金
    BigDecimal accrualRevise;         //对应第三方的贴息或扣息 (积木盒子)
    String borrowerName;              //还款人
	
    public BigDecimal getCurrentAccrual() {
		return currentAccrual;
	}
	public void setCurrentAccrual(BigDecimal currentAccrual) {
		this.currentAccrual = currentAccrual;
	}
	public Long getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}
	public BigDecimal getGiveBackRate() {
		return giveBackRate;
	}
	public void setGiveBackRate(BigDecimal giveBackRate) {
		this.giveBackRate = giveBackRate;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
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
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public BigDecimal getDeficit() {
		return deficit;
	}
	public void setDeficit(BigDecimal deficit) {
		this.deficit = deficit;
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
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
}
