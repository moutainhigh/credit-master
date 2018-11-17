package com.zdmoney.credit.loan.vo;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanReturnVo{
	private long id;
    private Long loanId;

    private Long currentTerm;

    private String importReason;

    private String batchNum;

    private String fundsSources;

    private String amount;
    private String pactMoney;
    private  String name;
    private String idnum;
    private String loanType;
    private String signDate;
    
    
    private Date createTime;
    private String start;
    private String end;
    private String importStart;
    private String importEnd;
    private String contractNum;
    private Date buyBackTime;
    private String loanBelongs;

    
	public String getImportStart() {
		return importStart;
	}

	public void setImportStart(String importStart) {
		this.importStart = importStart;
	}

	public String getImportEnd() {
		return importEnd;
	}

	public void setImportEnd(String importEnd) {
		this.importEnd = importEnd;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}

	public String getImportReason() {
		return importReason;
	}

	public void setImportReason(String importReason) {
		this.importReason = importReason;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(String pactMoney) {
		this.pactMoney = pactMoney;
	}

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



	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getLoanBelongs() {
		return loanBelongs;
	}

	public void setLoanBelongs(String loanBelongs) {
		this.loanBelongs = loanBelongs;
	}

	public Date getBuyBackTime() {
		return buyBackTime;
	}

	public void setBuyBackTime(Date buyBackTime) {
		this.buyBackTime = buyBackTime;
	}
    
}
