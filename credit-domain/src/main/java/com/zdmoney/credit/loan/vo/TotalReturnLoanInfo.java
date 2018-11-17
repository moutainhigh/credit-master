package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;

public class TotalReturnLoanInfo {
	private BigDecimal totalAmount; 
	private BigDecimal totalPactMoney;
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getTotalPactMoney() {
		return totalPactMoney;
	}
	public void setTotalPactMoney(BigDecimal totalPactMoney) {
		this.totalPactMoney = totalPactMoney;
	} 
	
}
