package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
/**
 * 逾期的本金和利息，提前结清的本金和利息
 */
public class AmountTypeVO {

	private Long loanId; 			// 借款ID
	private BigDecimal overdueCapital = BigDecimal.ZERO;//逾期本金
	private BigDecimal overdueAint = BigDecimal.ZERO;//逾期利息
	private BigDecimal clearCapital = BigDecimal.ZERO;//结清本金
	private BigDecimal clearAint = BigDecimal.ZERO;//结清利息

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getOverdueCapital() {
		return overdueCapital;
	}

	public void setOverdueCapital(BigDecimal overdueCapital) {
		this.overdueCapital = overdueCapital;
	}

	public BigDecimal getOverdueAint() {
		return overdueAint;
	}

	public void setOverdueAint(BigDecimal overdueAint) {
		this.overdueAint = overdueAint;
	}

	public BigDecimal getClearCapital() {
		return clearCapital;
	}

	public void setClearCapital(BigDecimal clearCapital) {
		this.clearCapital = clearCapital;
	}

	public BigDecimal getClearAint() {
		return clearAint;
	}

	public void setClearAint(BigDecimal clearAint) {
		this.clearAint = clearAint;
	}
	
	
}
