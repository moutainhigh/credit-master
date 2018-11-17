package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;

/**
 * 债权导出txt（供理财）
 * @author wangn  2016年9月27日 下午3:06:46
 *
 */
public class VLoanDebtFileInfo {
	
	private String id;
	private String orgCode;//机构代码
	private String contractNum; // 合同号
	private String applyAreaCode; //申请地点（码值）
	private String applyNum; //申请号
	private String channelSource; //渠道来源
	private String idType; //证件类型 码值
	private String idNum; //证件号码
	private String borrowName; //姓名
	private String telephone; //联系电话
	private String mobilePhone; //移动电话
	private String postCode; //邮政编码
	private String address; // 通讯地址
	private String purpose; // 申请用途
	private BigDecimal contractMoney; //合同金额
	private BigDecimal actualMoney; //实付金额
	private String currency; //申请币种 码值
	private String applyTerm; //借款申请期限（月）
	private String repayAccountType; //还款账户类型 码值
	private String repayAccountNum; //还款账号
	private String repayType; //还款方式 码值
	private String repayDayType; //扣款日类型 01-放款日为扣款日 02-固定扣款日
	private String repayDayCategory; //扣款日类别 01-对日  02-对月 03...
	private String repayDay; //每月应还款日 取天数
	private String marryStatus; //婚姻状况 码值
	private String edLevel; //学历 码值
	private String hrAddressCode; //户籍地址 码值
	private BigDecimal personMonthIncome; //个人月收入
	private String familyAddress; //家庭住址
	private String familyTelephone; //住宅电话
	private String familyPostCode; //住宅邮编
	private String handleOrgName; //经办机构
	private String paymentMethod; //缴费方式 01-期缴按月还  02	趸缴-一次性还清  99-其他
	private String loanType; //贷款类型 码值
	private String borrowerType; //借款人类型 码值
	private String productNum; //产品编号
	private String productType; //产品名称
	private BigDecimal handlingCharge; //手续费
	private BigDecimal chargeRate; //手续费率
	private BigDecimal accrualEM; //月利率
	private BigDecimal prepayPenalRate; //提前还款违约金比率
	private BigDecimal penaltyRate; //罚息利率（月）
	private String guaranteeDays; //履行担保天数
	private BigDecimal serviceMoney; //服务费
	private BigDecimal serviceRate; //服务费率
	private BigDecimal guaranteeMoney; //担保费
	private BigDecimal guaranteeRate;//担保费率
	private String repayBankCode; //还款银行编码码
	private String bankCityCode; //开户行 省市编码
	private BigDecimal fare1;
	private BigDecimal fare2;
	private BigDecimal fare3;
	private BigDecimal fare4;
	private BigDecimal fare5;
	private String lendDate; //放款日期
	private String lendAccountType; //放款账户类型 码值
	private String lendBankCode; //放款银行编码
	private String lendAccountName; //放款账户名称
	private String lendAccountNum; //放款账户号码
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getApplyAreaCode() {
		return applyAreaCode;
	}
	public void setApplyAreaCode(String applyAreaCode) {
		this.applyAreaCode = applyAreaCode;
	}
	public String getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getChannelSource() {
		return channelSource;
	}
	public void setChannelSource(String channelSource) {
		this.channelSource = channelSource;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getBorrowName() {
		return borrowName;
	}
	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public BigDecimal getContractMoney() {
		return contractMoney;
	}
	public void setContractMoney(BigDecimal contractMoney) {
		this.contractMoney = contractMoney;
	}
	public BigDecimal getActualMoney() {
		return actualMoney;
	}
	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getApplyTerm() {
		return applyTerm;
	}
	public void setApplyTerm(String applyTerm) {
		this.applyTerm = applyTerm;
	}
	public String getRepayAccountType() {
		return repayAccountType;
	}
	public void setRepayAccountType(String repayAccountType) {
		this.repayAccountType = repayAccountType;
	}
	public String getRepayAccountNum() {
		return repayAccountNum;
	}
	public void setRepayAccountNum(String repayAccountNum) {
		this.repayAccountNum = repayAccountNum;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepayDayType() {
		return repayDayType;
	}
	public void setRepayDayType(String repayDayType) {
		this.repayDayType = repayDayType;
	}
	public String getRepayDayCategory() {
		return repayDayCategory;
	}
	public void setRepayDayCategory(String repayDayCategory) {
		this.repayDayCategory = repayDayCategory;
	}
	public String getRepayDay() {
		return repayDay;
	}
	public void setRepayDay(String repayDay) {
		this.repayDay = repayDay;
	}

	public String getMarryStatus() {
		return marryStatus;
	}
	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}
	public String getEdLevel() {
		return edLevel;
	}
	public void setEdLevel(String edLevel) {
		this.edLevel = edLevel;
	}
	public String getHrAddressCode() {
		return hrAddressCode;
	}
	public void setHrAddressCode(String hrAddressCode) {
		this.hrAddressCode = hrAddressCode;
	}
	public BigDecimal getPersonMonthIncome() {
		return personMonthIncome;
	}
	public void setPersonMonthIncome(BigDecimal personMonthIncome) {
		this.personMonthIncome = personMonthIncome;
	}
	public String getFamilyAddress() {
		return familyAddress;
	}
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public String getFamilyTelephone() {
		return familyTelephone;
	}
	public void setFamilyTelephone(String familyTelephone) {
		this.familyTelephone = familyTelephone;
	}
	public String getHandleOrgName() {
		return handleOrgName;
	}
	public void setHandleOrgName(String handleOrgName) {
		this.handleOrgName = handleOrgName;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getBorrowerType() {
		return borrowerType;
	}
	public void setBorrowerType(String borrowerType) {
		this.borrowerType = borrowerType;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public BigDecimal getHandlingCharge() {
		return handlingCharge;
	}
	public void setHandlingCharge(BigDecimal handlingCharge) {
		this.handlingCharge = handlingCharge;
	}
	public BigDecimal getChargeRate() {
		return chargeRate;
	}
	public void setChargeRate(BigDecimal chargeRate) {
		this.chargeRate = chargeRate;
	}
	public BigDecimal getAccrualEM() {
		return accrualEM;
	}
	public void setAccrualEM(BigDecimal accrualEM) {
		this.accrualEM = accrualEM;
	}
	public BigDecimal getPrepayPenalRate() {
		return prepayPenalRate;
	}
	public void setPrepayPenalRate(BigDecimal prepayPenalRate) {
		this.prepayPenalRate = prepayPenalRate;
	}
	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}
	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
	}
	public String getGuaranteeDays() {
		return guaranteeDays;
	}
	public void setGuaranteeDays(String guaranteeDays) {
		this.guaranteeDays = guaranteeDays;
	}
	public BigDecimal getServiceMoney() {
		return serviceMoney;
	}
	public void setServiceMoney(BigDecimal serviceMoney) {
		this.serviceMoney = serviceMoney;
	}
	public BigDecimal getServiceRate() {
		return serviceRate;
	}
	public void setServiceRate(BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}
	public BigDecimal getGuaranteeMoney() {
		return guaranteeMoney;
	}
	public void setGuaranteeMoney(BigDecimal guaranteeMoney) {
		this.guaranteeMoney = guaranteeMoney;
	}
	public BigDecimal getGuaranteeRate() {
		return guaranteeRate;
	}
	public void setGuaranteeRate(BigDecimal guaranteeRate) {
		this.guaranteeRate = guaranteeRate;
	}
	public String getRepayBankCode() {
		return repayBankCode;
	}
	public void setRepayBankCode(String repayBankCode) {
		this.repayBankCode = repayBankCode;
	}
	public String getBankCityCode() {
		return bankCityCode;
	}
	public void setBankCityCode(String bankCityCode) {
		this.bankCityCode = bankCityCode;
	}
	public BigDecimal getFare1() {
		return fare1;
	}
	public void setFare1(BigDecimal fare1) {
		this.fare1 = fare1;
	}
	public BigDecimal getFare2() {
		return fare2;
	}
	public void setFare2(BigDecimal fare2) {
		this.fare2 = fare2;
	}
	public BigDecimal getFare3() {
		return fare3;
	}
	public void setFare3(BigDecimal fare3) {
		this.fare3 = fare3;
	}
	public BigDecimal getFare4() {
		return fare4;
	}
	public void setFare4(BigDecimal fare4) {
		this.fare4 = fare4;
	}
	public BigDecimal getFare5() {
		return fare5;
	}
	public void setFare5(BigDecimal fare5) {
		this.fare5 = fare5;
	}
	public String getLendDate() {
		return lendDate;
	}
	public void setLendDate(String lendDate) {
		this.lendDate = lendDate;
	}
	public String getLendAccountType() {
		return lendAccountType;
	}
	public void setLendAccountType(String lendAccountType) {
		this.lendAccountType = lendAccountType;
	}
	public String getLendBankCode() {
		return lendBankCode;
	}
	public void setLendBankCode(String lendBankCode) {
		this.lendBankCode = lendBankCode;
	}
	public String getLendAccountName() {
		return lendAccountName;
	}
	public void setLendAccountName(String lendAccountName) {
		this.lendAccountName = lendAccountName;
	}
	public String getLendAccountNum() {
		return lendAccountNum;
	}
	public void setLendAccountNum(String lendAccountNum) {
		this.lendAccountNum = lendAccountNum;
	}
	public String getFamilyPostCode() {
		return familyPostCode;
	}
	public void setFamilyPostCode(String familyPostCode) {
		this.familyPostCode = familyPostCode;
	}
	
	
}
