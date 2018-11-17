package com.zdmoney.credit.fee.vo;

import java.math.BigDecimal;

/**
 * 生成服务费报盘 Vo
 * 
 * @author Ivan
 *
 */
public class CreateFeeOfferVo {
	/** 服务费编号 **/
	private Long feeId;
	/** 服务费报盘金额 **/
	private BigDecimal amount = BigDecimal.ZERO;
	/** 合同来源 **/
	private String fundsSources; 
	
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

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}
	
}
