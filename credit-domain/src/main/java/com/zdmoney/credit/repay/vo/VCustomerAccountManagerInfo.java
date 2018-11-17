package com.zdmoney.credit.repay.vo;

public class VCustomerAccountManagerInfo {
	private Long accountId;
	
	private Long customerId;
	private int  zhuxuedaiOrg;
	public int getZhuxuedaiOrg() {
		return zhuxuedaiOrg;
	}
	public void setZhuxuedaiOrg(int  zhuxuedaiOrg) {
		this.zhuxuedaiOrg = zhuxuedaiOrg;
	}
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
}
