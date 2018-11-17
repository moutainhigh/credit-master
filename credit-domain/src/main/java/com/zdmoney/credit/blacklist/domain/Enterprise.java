package com.zdmoney.credit.blacklist.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Enterprise {
	
	String guId;     //GUID，主键
    String name;     //企业名称
    String legalName; //法人代表姓名
    String legalIdCard;  //法人代表身份证号
    String address; //企业地址
    String licenseCode; //执照号
    String telePhone;    //企业电话
    String riskLevel;  //风险级别值   1：一级（逾期3期以下），关注类客户； 2：二级（逾期3-6期; 外部导入的不良客户），拒贷类客户； 3：三级（逾期6期以上坏帐客户; 欺诈，恶意拖欠，恶意投诉），拒贷类客户
    String riskCase;   //风险原因/风险信息
    String riskTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());   //风险发现时间
    String infoSource;  //信息来源
    String createOrgan;  //创建机构
    String creator;      //创建人
    String updateUser;  //修改人
    
    
	public String getGuId() {
		return guId;
	}
	public void setGuId(String guId) {
		this.guId = guId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getLegalIdCard() {
		return legalIdCard;
	}
	public void setLegalIdCard(String legalIdCard) {
		this.legalIdCard = legalIdCard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLicenseCode() {
		return licenseCode;
	}
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getRiskCase() {
		return riskCase;
	}
	public void setRiskCase(String riskCase) {
		this.riskCase = riskCase;
	}
	public String getRiskTime() {
		return riskTime;
	}
	public void setRiskTime(String riskTime) {
		this.riskTime = riskTime;
	}
	public String getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}
	public String getCreateOrgan() {
		return createOrgan;
	}
	public void setCreateOrgan(String createOrgan) {
		this.createOrgan = createOrgan;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
}
