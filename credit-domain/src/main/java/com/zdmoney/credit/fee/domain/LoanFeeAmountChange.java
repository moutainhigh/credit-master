package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanFeeAmountChange extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6421338751100617223L;
	/** PK主键 **/
	private Long id;
	/** 记账ID **/
	private Long repayId;
	/** 已收金额 **/
	private BigDecimal receiveAmount;
	/** 未收金额 **/
	private BigDecimal unpaidAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRepayId() {
		return repayId;
	}

	public void setRepayId(Long repayId) {
		this.repayId = repayId;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}
}