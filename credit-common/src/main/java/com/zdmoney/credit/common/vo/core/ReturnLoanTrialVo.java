package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

public class ReturnLoanTrialVo {
	Long currentTerm;                //当前期数
    String returnDate;                    //还款日期
    BigDecimal repaymentAll;            //一次性还款金额
    BigDecimal returneterm;            //每期还款金额
	
    public Long getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
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
	public BigDecimal getReturneterm() {
		return returneterm;
	}
	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}
}
