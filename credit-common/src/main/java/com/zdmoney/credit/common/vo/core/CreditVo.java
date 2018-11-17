package com.zdmoney.credit.common.vo.core;

public class CreditVo {

	private String idnum;
	private String name;
	private String userCode;
	private String searchSource;
	private String searchCode;
	
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
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
	public String getSearchSource() {
		return searchSource;
	}
	public void setSearchSource(String searchSource) {
		this.searchSource = searchSource;
	}
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	
	@Override
	public String toString() {
		return "CreditVo [idnum=" + idnum + ", name=" + name + ", userCode=" + userCode + ", searchSource=" + searchSource + ", searchCode=" + searchCode + "]";
	}
}
