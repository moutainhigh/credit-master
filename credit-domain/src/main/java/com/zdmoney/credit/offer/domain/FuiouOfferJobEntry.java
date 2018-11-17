package com.zdmoney.credit.offer.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class FuiouOfferJobEntry extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8455541710813750369L;
	
	private String bankCode;
	private String name;
	private String idNum;
	private String idType;
	private String account;
	private String mphone;
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
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
	
	

}
