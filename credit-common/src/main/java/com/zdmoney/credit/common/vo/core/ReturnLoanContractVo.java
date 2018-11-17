package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装返回合同信息的参数
 * @author 00234770
 * @date 2015年8月28日 上午10:18:25 
 *
 */
public class ReturnLoanContractVo {

	String contractNum;     //合同编号
    String borrowerName;  //借款者姓名
    String borrowerName2;  //共同借款人姓名
    String sex; //性别
    String idnum;  //借款者证件号码
    String idnum2;  //共同借款人证件号码
    String address;  //住址
    String postcode; //邮编
    String email;
    Date signDate;   //签约日期
    Date startRDate;  //开始还款日期
    Date endRDate;   //最后还款日期
    Long time;  //期数
    String signingSite; //签约地点
    BigDecimal pactMoney;   //合同金额
    BigDecimal returnETerm;  //每期还款
    Long  promiseReturnDate; //约定还款日
    BigDecimal referRate;  //咨询费
    BigDecimal evalRate; //评估费
    BigDecimal manageRate; //管理费
    BigDecimal risk; //风险金
    BigDecimal managerRateForPartyC; //丙方管理费
    BigDecimal rateSum;  //收费合计
    BigDecimal giveBackRateFor3Term;  //第三期退费
    BigDecimal giveBackRateFor4Term;  //第四期退费
    BigDecimal giveBackRateAfter4Term; //第四期后每期退费
    String serviceTel; //服务电话
    BigDecimal overduePenalty1Day; //逾期1日罚息
    BigDecimal overduePenalty15Day; //  逾期15日罚息
    String purpose; //借款用途
    String contractVersion;  //合同版本
    String bank;    //银行名称
    String bankFullName;  //银行支行
    String account; //银行账号
    String giveBackBank; //还款银行
    String gbFullName; //还款支行
    String gbAccount; // 还款账户

    BigDecimal returnETermForT1;  //一期月还款
    Long timeForT1; //第一期期数
    Date startRDateForT1; //第一期还款开始日期
    Date endRDateForT1; //第一期还款结束日期
    BigDecimal returnETermForT2;  //二期月还款
    Long timeForT2; //第二期期数
    Date startRDateForT2; //第一期还款开始日期
    Date endRDateForT2; //第一期还款结束日期
    String orgName; //名称
    String zhongTaiSequence; //中泰信托合同序列号
    String xtjhSequence; //信托计划合同序列号

    Long loanId;
    
    BigDecimal giveBackRateFor1Term;  //第一期退费

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getBorrowerName2() {
		return borrowerName2;
	}

	public void setBorrowerName2(String borrowerName2) {
		this.borrowerName2 = borrowerName2;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getIdnum2() {
		return idnum2;
	}

	public void setIdnum2(String idnum2) {
		this.idnum2 = idnum2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getStartRDate() {
		return startRDate;
	}

	public void setStartRDate(Date startRDate) {
		this.startRDate = startRDate;
	}

	public Date getEndRDate() {
		return endRDate;
	}

	public void setEndRDate(Date endRDate) {
		this.endRDate = endRDate;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getSigningSite() {
		return signingSite;
	}

	public void setSigningSite(String signingSite) {
		this.signingSite = signingSite;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}

	public BigDecimal getReturnETerm() {
		return returnETerm;
	}

	public void setReturnETerm(BigDecimal returnETerm) {
		this.returnETerm = returnETerm;
	}

	public Long getPromiseReturnDate() {
		return promiseReturnDate;
	}

	public void setPromiseReturnDate(Long promiseReturnDate) {
		this.promiseReturnDate = promiseReturnDate;
	}

	public BigDecimal getReferRate() {
		return referRate;
	}

	public void setReferRate(BigDecimal referRate) {
		this.referRate = referRate;
	}

	public BigDecimal getEvalRate() {
		return evalRate;
	}

	public void setEvalRate(BigDecimal evalRate) {
		this.evalRate = evalRate;
	}

	public BigDecimal getManageRate() {
		return manageRate;
	}

	public void setManageRate(BigDecimal manageRate) {
		this.manageRate = manageRate;
	}

	public BigDecimal getRisk() {
		return risk;
	}

	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}

	public BigDecimal getManagerRateForPartyC() {
		return managerRateForPartyC;
	}

	public void setManagerRateForPartyC(BigDecimal managerRateForPartyC) {
		this.managerRateForPartyC = managerRateForPartyC;
	}

	public BigDecimal getRateSum() {
		return rateSum;
	}

	public void setRateSum(BigDecimal rateSum) {
		this.rateSum = rateSum;
	}

	public BigDecimal getGiveBackRateFor3Term() {
		return giveBackRateFor3Term;
	}

	public void setGiveBackRateFor3Term(BigDecimal giveBackRateFor3Term) {
		this.giveBackRateFor3Term = giveBackRateFor3Term;
	}

	public BigDecimal getGiveBackRateFor4Term() {
		return giveBackRateFor4Term;
	}

	public void setGiveBackRateFor4Term(BigDecimal giveBackRateFor4Term) {
		this.giveBackRateFor4Term = giveBackRateFor4Term;
	}

	public BigDecimal getGiveBackRateAfter4Term() {
		return giveBackRateAfter4Term;
	}

	public void setGiveBackRateAfter4Term(BigDecimal giveBackRateAfter4Term) {
		this.giveBackRateAfter4Term = giveBackRateAfter4Term;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public BigDecimal getOverduePenalty1Day() {
		return overduePenalty1Day;
	}

	public void setOverduePenalty1Day(BigDecimal overduePenalty1Day) {
		this.overduePenalty1Day = overduePenalty1Day;
	}

	public BigDecimal getOverduePenalty15Day() {
		return overduePenalty15Day;
	}

	public void setOverduePenalty15Day(BigDecimal overduePenalty15Day) {
		this.overduePenalty15Day = overduePenalty15Day;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getContractVersion() {
		return contractVersion;
	}

	public void setContractVersion(String contractVersion) {
		this.contractVersion = contractVersion;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getGiveBackBank() {
		return giveBackBank;
	}

	public void setGiveBackBank(String giveBackBank) {
		this.giveBackBank = giveBackBank;
	}

	public String getGbFullName() {
		return gbFullName;
	}

	public void setGbFullName(String gbFullName) {
		this.gbFullName = gbFullName;
	}

	public String getGbAccount() {
		return gbAccount;
	}

	public void setGbAccount(String gbAccount) {
		this.gbAccount = gbAccount;
	}

	public BigDecimal getReturnETermForT1() {
		return returnETermForT1;
	}

	public void setReturnETermForT1(BigDecimal returnETermForT1) {
		this.returnETermForT1 = returnETermForT1;
	}

	public Long getTimeForT1() {
		return timeForT1;
	}

	public void setTimeForT1(Long timeForT1) {
		this.timeForT1 = timeForT1;
	}

	public Date getStartRDateForT1() {
		return startRDateForT1;
	}

	public void setStartRDateForT1(Date startRDateForT1) {
		this.startRDateForT1 = startRDateForT1;
	}

	public Date getEndRDateForT1() {
		return endRDateForT1;
	}

	public void setEndRDateForT1(Date endRDateForT1) {
		this.endRDateForT1 = endRDateForT1;
	}

	public BigDecimal getReturnETermForT2() {
		return returnETermForT2;
	}

	public void setReturnETermForT2(BigDecimal returnETermForT2) {
		this.returnETermForT2 = returnETermForT2;
	}

	public Long getTimeForT2() {
		return timeForT2;
	}

	public void setTimeForT2(Long timeForT2) {
		this.timeForT2 = timeForT2;
	}

	public Date getStartRDateForT2() {
		return startRDateForT2;
	}

	public void setStartRDateForT2(Date startRDateForT2) {
		this.startRDateForT2 = startRDateForT2;
	}

	public Date getEndRDateForT2() {
		return endRDateForT2;
	}

	public void setEndRDateForT2(Date endRDateForT2) {
		this.endRDateForT2 = endRDateForT2;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getZhongTaiSequence() {
		return zhongTaiSequence;
	}

	public void setZhongTaiSequence(String zhongTaiSequence) {
		this.zhongTaiSequence = zhongTaiSequence;
	}

	public String getXtjhSequence() {
		return xtjhSequence;
	}

	public void setXtjhSequence(String xtjhSequence) {
		this.xtjhSequence = xtjhSequence;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getGiveBackRateFor1Term() {
		return giveBackRateFor1Term;
	}

	public void setGiveBackRateFor1Term(BigDecimal giveBackRateFor1Term) {
		this.giveBackRateFor1Term = giveBackRateFor1Term;
	}

}
