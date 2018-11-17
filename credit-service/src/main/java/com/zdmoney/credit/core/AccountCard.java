package com.zdmoney.credit.core;

import java.math.BigDecimal;

import com.zdmoney.credit.offer.domain.OfferRepayInfo;

public class AccountCard {
	OfferRepayInfo repayInfo;
	BigDecimal qichuBalance;//期初余额
    BigDecimal income;//收入
    BigDecimal outlay;//支出
    BigDecimal qimoBalance;//期末余额
	
    public OfferRepayInfo getRepayInfo() {
		return repayInfo;
	}
	public void setRepayInfo(OfferRepayInfo repayInfo) {
		this.repayInfo = repayInfo;
	}
	public BigDecimal getQichuBalance() {
		return qichuBalance;
	}
	public void setQichuBalance(BigDecimal qichuBalance) {
		this.qichuBalance = qichuBalance;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getOutlay() {
		return outlay;
	}
	public void setOutlay(BigDecimal outlay) {
		this.outlay = outlay;
	}
	public BigDecimal getQimoBalance() {
		return qimoBalance;
	}
	public void setQimoBalance(BigDecimal qimoBalance) {
		this.qimoBalance = qimoBalance;
	}
}
