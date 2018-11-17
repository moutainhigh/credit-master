package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款收费主表
 * 
 * @author Ivan
 *
 */
public class LoanFeeInfo extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8053943840616127797L;
	/** 主键 **/
	private Long id;
	/** 债权编号 **/
	private Long loanId;
	/** 应收金额 **/
	private BigDecimal amount;
	/** 已收金额 **/
	private BigDecimal receiveAmount;
	/** 未收金额 **/
	private BigDecimal unpaidAmount;
	/** 状态（未收取、已收取、部分收取） **/
	private String state;
	/** 预留备注信息 **/
	private String memo;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
}