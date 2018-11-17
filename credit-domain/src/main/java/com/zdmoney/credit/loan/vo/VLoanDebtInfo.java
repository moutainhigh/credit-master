package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;
import java.util.Date;

public class VLoanDebtInfo {
    
    private String id;
    
    private String contract_num;
    
    private String loan_type;
    
    private String purpose;
    
    private BigDecimal pact_money;
    
    private Long time;
    
    private Date startrdate;
    
    private String acct_name;
    
    private String borrower_sex;
    
    private String borrower_idnum;
    
    private Long backTerm;
    
    private String batchNum;
    
    private String bank_name;
    
    private String full_name;
    
    private String account;
    
    private BigDecimal grant_money;
    
    private String fuwufei;
    
    private String returneterm;
    
    private Date sign_Date;
    
    private Date endRDate;
    private String name;
	public String signSalesDep_fullName;
    /**
     *录单营业部
     */
    private String applyInputFlag ;
    
    public String getSignSalesDep_fullName() {
		return signSalesDep_fullName;
	}

	public void setSignSalesDep_fullName(String signSalesDep_fullName) {
		this.signSalesDep_fullName = signSalesDep_fullName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContract_num() {
        return contract_num;
    }

    public void setContract_num(String contract_num) {
        this.contract_num = contract_num;
    }

    public String getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(String loan_type) {
        this.loan_type = loan_type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getPact_money() {
        return pact_money;
    }

    public void setPact_money(BigDecimal pact_money) {
        this.pact_money = pact_money;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getStartrdate() {
        return startrdate;
    }

    public void setStartrdate(Date startrdate) {
        this.startrdate = startrdate;
    }

    public String getAcct_name() {
        return acct_name;
    }

    public void setAcct_name(String acct_name) {
        this.acct_name = acct_name;
    }

    public String getBorrower_sex() {
        return borrower_sex;
    }

    public void setBorrower_sex(String borrower_sex) {
        this.borrower_sex = borrower_sex;
    }

    public String getBorrower_idnum() {
        return borrower_idnum;
    }

    public void setBorrower_idnum(String borrower_idnum) {
        this.borrower_idnum = borrower_idnum;
    }

    public Long getBackTerm() {
        return backTerm;
    }

    public void setBackTerm(Long backTerm) {
        this.backTerm = backTerm;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getGrant_money() {
        return grant_money;
    }

    public void setGrant_money(BigDecimal grant_money) {
        this.grant_money = grant_money;
    }

    public String getFuwufei() {
        return fuwufei;
    }

    public void setFuwufei(String fuwufei) {
        this.fuwufei = fuwufei;
    }

    public String getReturneterm() {
        return returneterm;
    }

    public void setReturneterm(String returneterm) {
        this.returneterm = returneterm;
    }

	public Date getSign_Date() {
		return sign_Date;
	}

	public void setSign_Date(Date sign_Date) {
		this.sign_Date = sign_Date;
	}

	public Date getEndRDate() {
		return endRDate;
	}

	public void setEndRDate(Date endRDate) {
		this.endRDate = endRDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getApplyInputFlag() {
        return applyInputFlag;
    }

    public void setApplyInputFlag(String applyInputFlag) {
        this.applyInputFlag = applyInputFlag;
    }
}
