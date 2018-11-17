package com.zdmoney.credit.xintuo.domain;

import com.zdmoney.credit.common.util.exceldata.XintuoData;
import com.zdmoney.credit.framework.domain.BaseDomain;

public class XintuoDataDomain extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3279795137548504353L;

	String name;
    String idnum;
    String phone;
    String account;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public XintuoData toXintuoData() {
		XintuoData newInstance = new XintuoData();
		newInstance.setName(this.name);
		newInstance.setIdnum(this.idnum);
		newInstance.setPhone(this.phone);
		newInstance.setAccount(this.account);
		return newInstance;
	}
}
