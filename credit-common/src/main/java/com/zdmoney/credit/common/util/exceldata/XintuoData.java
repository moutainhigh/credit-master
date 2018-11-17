package com.zdmoney.credit.common.util.exceldata;

import java.io.Serializable;


/**
 * 用于多张表联立过得的数据,参考jimuDate
 * Created by Administrator
 */
public class XintuoData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5806435710164270159L;
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
    
}
