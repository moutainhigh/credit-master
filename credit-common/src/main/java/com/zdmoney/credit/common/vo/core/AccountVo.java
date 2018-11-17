package com.zdmoney.credit.common.vo.core;

public class AccountVo {

	private String bankCode;
	private String branchBankCode;
	private String bankDicType;
	private String account;
	private String mphone;
	private String bankId;
	private String idnum;
	private String sex;
	private String name;
	private String userCode;
	
	private String remark;
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchBankCode() {
		return branchBankCode;
	}
	public void setBranchBankCode(String branchBankCode) {
		this.branchBankCode = branchBankCode;
	}
	public String getBankDicType() {
		return bankDicType;
	}
	public void setBankDicType(String bankDicType) {
		this.bankDicType = bankDicType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "AccountVo [bankCode=" + bankCode + ", branchBankCode="
				+ branchBankCode + ", bankDicType=" + bankDicType
				+ ", account=" + account + ", mphone=" + mphone + ", bankId="
				+ bankId + ", idnum=" + idnum + ", sex=" + sex + ", name="
				+ name + ", userCode=" + userCode + ", remark=" + remark + "]";
	}
	
	
}
