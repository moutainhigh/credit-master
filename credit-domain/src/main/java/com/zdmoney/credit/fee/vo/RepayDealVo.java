package com.zdmoney.credit.fee.vo;

import java.math.BigDecimal;

/**
 * 服务费记账Vo
 * 
 * @author Ivan
 *
 */
public class RepayDealVo {
	/** 服务费编号 **/
	private Long feeId;
	/** 报盘编号 **/
	private Long offerId;
	/** 记账金额 **/
	private BigDecimal amount;
	/** 交易类型（转账、现金、通联、银联） **/
	private String tradeType;
	/** 备注 **/
	private String memo;

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
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
		this.tradeType = tradeType;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
