package com.zdmoney.credit.common.vo.core;

public class LoginCheckVo {
	/** 员工工号 **/
	private String userCode;
	/** 登陆密码 **/
	private String passWord;
	/** 员工姓名 **/
	private String name;
	/** 员工PK **/
	private Long id;
	/** 是否首次登陆 **/
	private String isFirst;
	/** 区域/分部 **/
	private String areaInfo;
	/** 营业部 **/
	private String branchInfo;
	/** 所属业务组 **/
	private String groupInfo;
	/** 手机号 **/
	private String mobile;
	/** 门店代码 **/
	private String orgCode;
	/** 员工类型 1.客户经理 2.客服 3.未知 **/
	private String userType;
	/** 区分新旧系统标识 :n,o**/
	private String newOrOld;	

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getNewOrOld() {
		return newOrOld;
	}

	public void setNewOrOld(String newOrOld) {
		this.newOrOld = newOrOld;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	public String getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}

	public String getBranchInfo() {
		return branchInfo;
	}

	public void setBranchInfo(String branchInfo) {
		this.branchInfo = branchInfo;
	}

	public String getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "{LoginCheckVoInfo:{userCode=" + userCode + ", name=" + name + ",id=" + id + ",orgCode="+orgCode+",userType="+userType+"}}";
	}

}
