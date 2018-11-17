package com.zdmoney.credit.offer.vo;

import java.math.BigDecimal;

/**
 * 实时划扣
 * @author Anfq
 *
 * 2015年8月25日
 */
public class OfferRealTimeDeduct {
	private String borrowName;
	private String borrowIdum;
	private BigDecimal offerAmount=BigDecimal.ZERO;
	 private Long loanId;
	public String getBorrowName() {
		return borrowName;
	}
	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	public String getBorrowIdum() {
		return borrowIdum;
	}
	public void setBorrowIdum(String borrowIdum) {
		this.borrowIdum = borrowIdum;
	}
	
	
	public BigDecimal getOfferAmount() {
		return offerAmount;
	}
	public void setOfferAmount(BigDecimal offerAmount) {
		this.offerAmount = offerAmount;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	

}
