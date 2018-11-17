package com.zdmoney.credit.repay.vo;

public class VCustomerAccountManagerRepaymentEarnestMoney {
	private Long accountId;
	
	private Long customerId;
	
	private String loanType;
	
	private Double amount;
	
	private String memo;
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" accountId:"+accountId);
		sb.append(" customerId:"+customerId);
		sb.append(" loanType:"+loanType);
		sb.append(" amount:"+amount);
		sb.append(" memo:"+memo);
		return sb.toString();
	}
}
