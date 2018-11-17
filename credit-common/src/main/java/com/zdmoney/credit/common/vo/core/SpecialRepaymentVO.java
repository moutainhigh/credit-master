package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

/**
 * 特殊还款查询接口返回值类型
 * 
 * @author 00235304
 * 
 */
public class SpecialRepaymentVO {

	/** 债权ID */
	private Long loanId;

	/** 减免金额 */
	private BigDecimal reliefAmount;

	/** 特殊还款类型 */
	private String spRepayType;

	/** 特殊还款状态 */
	private String spRepayState;

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getReliefAmount() {
		return reliefAmount;
	}

	public void setReliefAmount(BigDecimal reliefAmount) {
		this.reliefAmount = reliefAmount;
	}

	public String getSpRepayType() {
		return spRepayType;
	}

	public void setSpRepayType(String spRepayType) {
		this.spRepayType = spRepayType;
	}

	public String getSpRepayState() {
		return spRepayState;
	}

	public void setSpRepayState(String spRepayState) {
		this.spRepayState = spRepayState;
	}

}
