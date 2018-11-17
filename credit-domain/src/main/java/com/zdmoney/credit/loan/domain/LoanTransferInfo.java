package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanTransferInfo extends BaseDomain{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long loanId;

    private String manageDepartment;

    private String loanType;

    private String customerName;

    private String idNum;

    private Date signDate;

    private String promiseReturnDate;

    private BigDecimal pactMoney;

    private Date overdueStartDate;

    private Long overdueTerm;

    private Long overdueDay;

    private BigDecimal surplusCapital;

    private BigDecimal overdueCapital;

    private BigDecimal overdueAint;

    private Date fineStartDate;

    private BigDecimal fineAmount;

    private BigDecimal returnTotalAmount;

    private Date lastReturnDate;

    private String fundsSources;

    private String loanBelong;

    private String contractNum;

    private String transferBatch;
    
    private String creator;
    
    private Date createTime;

    
    public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManageDepartment() {
        return manageDepartment;
    }

    public void setManageDepartment(String manageDepartment) {
        this.manageDepartment = manageDepartment == null ? null : manageDepartment.trim();
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum == null ? null : idNum.trim();
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getPromiseReturnDate() {
        return promiseReturnDate;
    }

    public void setPromiseReturnDate(String promiseReturnDate) {
        this.promiseReturnDate = promiseReturnDate == null ? null : promiseReturnDate.trim();
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public Date getOverdueStartDate() {
        return overdueStartDate;
    }

    public void setOverdueStartDate(Date overdueStartDate) {
        this.overdueStartDate = overdueStartDate;
    }

    public Long getOverdueTerm() {
        return overdueTerm;
    }

    public void setOverdueTerm(Long overdueTerm) {
        this.overdueTerm = overdueTerm;
    }

    public Long getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(Long overdueDay) {
        this.overdueDay = overdueDay;
    }

    public BigDecimal getSurplusCapital() {
        return surplusCapital;
    }

    public void setSurplusCapital(BigDecimal surplusCapital) {
        this.surplusCapital = surplusCapital;
    }

    public BigDecimal getOverdueCapital() {
        return overdueCapital;
    }

    public void setOverdueCapital(BigDecimal overdueCapital) {
        this.overdueCapital = overdueCapital;
    }

    public BigDecimal getOverdueAint() {
        return overdueAint;
    }

    public void setOverdueAint(BigDecimal overdueAint) {
        this.overdueAint = overdueAint;
    }

    public Date getFineStartDate() {
        return fineStartDate;
    }

    public void setFineStartDate(Date fineStartDate) {
        this.fineStartDate = fineStartDate;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public BigDecimal getReturnTotalAmount() {
        return returnTotalAmount;
    }

    public void setReturnTotalAmount(BigDecimal returnTotalAmount) {
        this.returnTotalAmount = returnTotalAmount;
    }

    public Date getLastReturnDate() {
        return lastReturnDate;
    }

    public void setLastReturnDate(Date lastReturnDate) {
        this.lastReturnDate = lastReturnDate;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources == null ? null : fundsSources.trim();
    }

    public String getLoanBelong() {
        return loanBelong;
    }

    public void setLoanBelong(String loanBelong) {
        this.loanBelong = loanBelong == null ? null : loanBelong.trim();
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum == null ? null : contractNum.trim();
    }

    public String getTransferBatch() {
        return transferBatch;
    }

    public void setTransferBatch(String transferBatch) {
        this.transferBatch = transferBatch == null ? null : transferBatch.trim();
    }

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}