package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款收费记账表
 * 
 * @author Ivan
 *
 */
public class LoanFeeRepayInfo extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6614755113787764124L;
	/** 主键 **/
	private Long id;
	/** 债权编号 **/
	private Long loanId;
	/** 收费主表编号 **/
	private Long feeId;
	/** 报盘编号 **/
	private Long offerId;
	/** 流水号 **/
	private String serialNo;
	/** 记账时间 **/
	private Date tradeTime;
	/** 记账金额 **/
	private BigDecimal amount;
	/** 交易类型（转账、现金、通联、银联） **/
	private String tradeType;
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

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo == null ? null : serialNo.trim();
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType == null ? null : tradeType.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
}