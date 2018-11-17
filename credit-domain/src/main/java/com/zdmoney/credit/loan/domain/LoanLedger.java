package com.zdmoney.credit.loan.domain;


import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanLedger extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7203211189432705656L;

	private Long id;

    private String account;

    private BigDecimal amount = new BigDecimal(0);

    private BigDecimal appraisalExp= new BigDecimal(0);

    private BigDecimal appraisalIncome= new BigDecimal(0);

    private BigDecimal consultExp= new BigDecimal(0);

    private BigDecimal consultIncome= new BigDecimal(0);

    private BigDecimal fineExp= new BigDecimal(0);

    private BigDecimal fineIncome= new BigDecimal(0);

    private BigDecimal finePayable= new BigDecimal(0);

    private BigDecimal fineReceivable= new BigDecimal(0);

    private BigDecimal interestExp= new BigDecimal(0);

    private BigDecimal interestIncome= new BigDecimal(0);

    private BigDecimal interestPayable= new BigDecimal(0);

    private BigDecimal interestReceivable= new BigDecimal(0);

    private BigDecimal loanAmount= new BigDecimal(0);

    private BigDecimal manageExp= new BigDecimal(0);

    private BigDecimal manageIncome= new BigDecimal(0);

    private String memo;

    private BigDecimal nonOperatExp= new BigDecimal(0);

    private BigDecimal nonOperatIncome= new BigDecimal(0);

    private BigDecimal otherExp= new BigDecimal(0);

    private BigDecimal otherIncome= new BigDecimal(0);

    private BigDecimal otherPayable= new BigDecimal(0);

    private BigDecimal otherReceivale= new BigDecimal(0);

    private BigDecimal penaltyExp= new BigDecimal(0);

    private BigDecimal penaltyIncome= new BigDecimal(0);

    private String type;

    private Long loanId;



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAppraisalExp() {
        return appraisalExp;
    }

    public void setAppraisalExp(BigDecimal appraisalExp) {
        this.appraisalExp = appraisalExp;
    }

    public BigDecimal getAppraisalIncome() {
        return appraisalIncome;
    }

    public void setAppraisalIncome(BigDecimal appraisalIncome) {
        this.appraisalIncome = appraisalIncome;
    }

    public BigDecimal getConsultExp() {
        return consultExp;
    }

    public void setConsultExp(BigDecimal consultExp) {
        this.consultExp = consultExp;
    }

    public BigDecimal getConsultIncome() {
        return consultIncome;
    }

    public void setConsultIncome(BigDecimal consultIncome) {
        this.consultIncome = consultIncome;
    }

    public BigDecimal getFineExp() {
        return fineExp;
    }

    public void setFineExp(BigDecimal fineExp) {
        this.fineExp = fineExp;
    }

    public BigDecimal getFineIncome() {
        return fineIncome;
    }

    public void setFineIncome(BigDecimal fineIncome) {
        this.fineIncome = fineIncome;
    }

    public BigDecimal getFinePayable() {
        return finePayable;
    }

    public void setFinePayable(BigDecimal finePayable) {
        this.finePayable = finePayable;
    }

    public BigDecimal getFineReceivable() {
        return fineReceivable;
    }

    public void setFineReceivable(BigDecimal fineReceivable) {
        this.fineReceivable = fineReceivable;
    }

    public BigDecimal getInterestExp() {
        return interestExp;
    }

    public void setInterestExp(BigDecimal interestExp) {
        this.interestExp = interestExp;
    }

    public BigDecimal getInterestIncome() {
        return interestIncome;
    }

    public void setInterestIncome(BigDecimal interestIncome) {
        this.interestIncome = interestIncome;
    }

    public BigDecimal getInterestPayable() {
        return interestPayable;
    }

    public void setInterestPayable(BigDecimal interestPayable) {
        this.interestPayable = interestPayable;
    }

    public BigDecimal getInterestReceivable() {
        return interestReceivable;
    }

    public void setInterestReceivable(BigDecimal interestReceivable) {
        this.interestReceivable = interestReceivable;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getManageExp() {
        return manageExp;
    }

    public void setManageExp(BigDecimal manageExp) {
        this.manageExp = manageExp;
    }

    public BigDecimal getManageIncome() {
        return manageIncome;
    }

    public void setManageIncome(BigDecimal manageIncome) {
        this.manageIncome = manageIncome;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public BigDecimal getNonOperatExp() {
        return nonOperatExp;
    }

    public void setNonOperatExp(BigDecimal nonOperatExp) {
        this.nonOperatExp = nonOperatExp;
    }

    public BigDecimal getNonOperatIncome() {
        return nonOperatIncome;
    }

    public void setNonOperatIncome(BigDecimal nonOperatIncome) {
        this.nonOperatIncome = nonOperatIncome;
    }

    public BigDecimal getOtherExp() {
        return otherExp;
    }

    public void setOtherExp(BigDecimal otherExp) {
        this.otherExp = otherExp;
    }

    public BigDecimal getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(BigDecimal otherIncome) {
        this.otherIncome = otherIncome;
    }

    public BigDecimal getOtherPayable() {
        return otherPayable;
    }

    public void setOtherPayable(BigDecimal otherPayable) {
        this.otherPayable = otherPayable;
    }

    public BigDecimal getOtherReceivale() {
        return otherReceivale;
    }

    public void setOtherReceivale(BigDecimal otherReceivale) {
        this.otherReceivale = otherReceivale;
    }

    public BigDecimal getPenaltyExp() {
        return penaltyExp;
    }

    public void setPenaltyExp(BigDecimal penaltyExp) {
        this.penaltyExp = penaltyExp;
    }

    public BigDecimal getPenaltyIncome() {
        return penaltyIncome;
    }

    public void setPenaltyIncome(BigDecimal penaltyIncome) {
        this.penaltyIncome = penaltyIncome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

}