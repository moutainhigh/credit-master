package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanContract extends BaseDomain {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String account;

    private String address;

    private String bank;

    private String bankFullName;

    private String borrowerName;

    private String contractNum;

    private String contractVersion;

    private String email;

    private Date endrdate;

    private BigDecimal evalRate;

    private BigDecimal giveBackRateAfter4term;

    private BigDecimal giveBackRateFor3term;

    private BigDecimal giveBackRateFor4term;

    private String idnum;

    private Long loanId;

    private BigDecimal manageRate;

    private BigDecimal managerRateForPartyc;

    private BigDecimal overduePenalty15day;

    private BigDecimal overduePenalty1day;

    private BigDecimal pactMoney;

    private String postcode;

    private Long promiseReturnDate;

    private String purpose;

    private BigDecimal rateSum;

    private BigDecimal referRate;

    private BigDecimal returneterm;

    private String serviceTel;

    private String sex;

    private Date signDate;

    private String signingSite;

    private Date startrdate;

    private Long time;

    private Date endrdateFort1;

    private Date endrdateFort2;

    private String gbAccount;

    private String gbFullName;

    private String giveBackBank;

    private String orgName;

    private BigDecimal returnetermFort1;

    private BigDecimal returnetermFort2;

    private Date startrdateFort1;

    private Date startrdateFort2;

    private Long timeFort1;

    private Long timeFort2;

    private BigDecimal risk;

    private String borrowerName2;

    private String idnum2;

    private String zhongTaiSequence;

    private String xtjhSequence;

    private Date createTime;

    private Date updateTime;

    private String creator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getContractVersion() {
		return contractVersion;
	}

	public void setContractVersion(String contractVersion) {
		this.contractVersion = contractVersion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEndrdate() {
		return endrdate;
	}

	public void setEndrdate(Date endrdate) {
		this.endrdate = endrdate;
	}

	public BigDecimal getEvalRate() {
		return evalRate;
	}

	public void setEvalRate(BigDecimal evalRate) {
		this.evalRate = evalRate;
	}

	public BigDecimal getGiveBackRateAfter4term() {
		return giveBackRateAfter4term;
	}

	public void setGiveBackRateAfter4term(BigDecimal giveBackRateAfter4term) {
		this.giveBackRateAfter4term = giveBackRateAfter4term;
	}

	public BigDecimal getGiveBackRateFor3term() {
		return giveBackRateFor3term;
	}

	public void setGiveBackRateFor3term(BigDecimal giveBackRateFor3term) {
		this.giveBackRateFor3term = giveBackRateFor3term;
	}

	public BigDecimal getGiveBackRateFor4term() {
		return giveBackRateFor4term;
	}

	public void setGiveBackRateFor4term(BigDecimal giveBackRateFor4term) {
		this.giveBackRateFor4term = giveBackRateFor4term;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getManageRate() {
		return manageRate;
	}

	public void setManageRate(BigDecimal manageRate) {
		this.manageRate = manageRate;
	}

	public BigDecimal getManagerRateForPartyc() {
		return managerRateForPartyc;
	}

	public void setManagerRateForPartyc(BigDecimal managerRateForPartyc) {
		this.managerRateForPartyc = managerRateForPartyc;
	}

	public BigDecimal getOverduePenalty15day() {
		return overduePenalty15day;
	}

	public void setOverduePenalty15day(BigDecimal overduePenalty15day) {
		this.overduePenalty15day = overduePenalty15day;
	}

	public BigDecimal getOverduePenalty1day() {
		return overduePenalty1day;
	}

	public void setOverduePenalty1day(BigDecimal overduePenalty1day) {
		this.overduePenalty1day = overduePenalty1day;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Long getPromiseReturnDate() {
		return promiseReturnDate;
	}

	public void setPromiseReturnDate(Long promiseReturnDate) {
		this.promiseReturnDate = promiseReturnDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public BigDecimal getRateSum() {
		return rateSum;
	}

	public void setRateSum(BigDecimal rateSum) {
		this.rateSum = rateSum;
	}

	public BigDecimal getReferRate() {
		return referRate;
	}

	public void setReferRate(BigDecimal referRate) {
		this.referRate = referRate;
	}

	public BigDecimal getReturneterm() {
		return returneterm;
	}

	public void setReturneterm(BigDecimal returneterm) {
		this.returneterm = returneterm;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getSigningSite() {
		return signingSite;
	}

	public void setSigningSite(String signingSite) {
		this.signingSite = signingSite;
	}

	public Date getStartrdate() {
		return startrdate;
	}

	public void setStartrdate(Date startrdate) {
		this.startrdate = startrdate;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Date getEndrdateFort1() {
		return endrdateFort1;
	}

	public void setEndrdateFort1(Date endrdateFort1) {
		this.endrdateFort1 = endrdateFort1;
	}

	public Date getEndrdateFort2() {
		return endrdateFort2;
	}

	public void setEndrdateFort2(Date endrdateFort2) {
		this.endrdateFort2 = endrdateFort2;
	}

	public String getGbAccount() {
		return gbAccount;
	}

	public void setGbAccount(String gbAccount) {
		this.gbAccount = gbAccount;
	}

	public String getGbFullName() {
		return gbFullName;
	}

	public void setGbFullName(String gbFullName) {
		this.gbFullName = gbFullName;
	}

	public String getGiveBackBank() {
		return giveBackBank;
	}

	public void setGiveBackBank(String giveBackBank) {
		this.giveBackBank = giveBackBank;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public BigDecimal getReturnetermFort1() {
		return returnetermFort1;
	}

	public void setReturnetermFort1(BigDecimal returnetermFort1) {
		this.returnetermFort1 = returnetermFort1;
	}

	public BigDecimal getReturnetermFort2() {
		return returnetermFort2;
	}

	public void setReturnetermFort2(BigDecimal returnetermFort2) {
		this.returnetermFort2 = returnetermFort2;
	}

	public Date getStartrdateFort1() {
		return startrdateFort1;
	}

	public void setStartrdateFort1(Date startrdateFort1) {
		this.startrdateFort1 = startrdateFort1;
	}

	public Date getStartrdateFort2() {
		return startrdateFort2;
	}

	public void setStartrdateFort2(Date startrdateFort2) {
		this.startrdateFort2 = startrdateFort2;
	}

	public Long getTimeFort1() {
		return timeFort1;
	}

	public void setTimeFort1(Long timeFort1) {
		this.timeFort1 = timeFort1;
	}

	public Long getTimeFort2() {
		return timeFort2;
	}

	public void setTimeFort2(Long timeFort2) {
		this.timeFort2 = timeFort2;
	}

	public BigDecimal getRisk() {
		return risk;
	}

	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}

	public String getBorrowerName2() {
		return borrowerName2;
	}

	public void setBorrowerName2(String borrowerName2) {
		this.borrowerName2 = borrowerName2;
	}

	public String getIdnum2() {
		return idnum2;
	}

	public void setIdnum2(String idnum2) {
		this.idnum2 = idnum2;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}