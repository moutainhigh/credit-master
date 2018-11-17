package com.zdmoney.credit.system.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class Picture extends BaseDomain{
	private static final long serialVersionUID = -4725079163161790670L;
	private Long id;
	private Long org;
	private String imgName;
	private String saveNmae;
	private String sortSid;
	private String  subclassSort;
	private String appNo;
	private String contion;
	private String sysName;
	
	public String getContion() {
		return contion;
	}
	public void setContion(String contion) {
		this.contion = contion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrg() {
		return org;
	}
	public void setOrg(Long org) {
		this.org = org;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getSaveNmae() {
		return saveNmae;
	}
	public void setSaveNmae(String saveNmae) {
		this.saveNmae = saveNmae;
	}
	public String getSortSid() {
		return sortSid;
	}
	public void setSortSid(String sortSid) {
		this.sortSid = sortSid;
	}
	public String getSubclassSort() {
		return subclassSort;
	}
	public void setSubclassSort(String subclassSort) {
		this.subclassSort = subclassSort;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
	
}
