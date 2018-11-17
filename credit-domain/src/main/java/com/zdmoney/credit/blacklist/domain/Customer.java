package com.zdmoney.credit.blacklist.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer {
	String guId;     //GUID，主键
    String code ;    //客户编号
    String name;     //客户姓名
    String idCard;   //客户身份证号
    String spouseName;  //配偶姓名
    String spouseIdCard; //配偶身份证
    String mobilePhone; //手机
    String telePhone;    //电话
    String workUnit;   //工作单位
    String unitAddress; //单位地址
    String unitPhone;  //单位电话
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSpouseName() {
		return spouseName;
	}
	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}
	public String getSpouseIdCard() {
		return spouseIdCard;
	}
	public void setSpouseIdCard(String spouseIdCard) {
		this.spouseIdCard = spouseIdCard;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getUnitAddress() {
		return unitAddress;
	}
	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}
	public String getUnitPhone() {
		return unitPhone;
	}
	public void setUnitPhone(String unitPhone) {
		this.unitPhone = unitPhone;
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