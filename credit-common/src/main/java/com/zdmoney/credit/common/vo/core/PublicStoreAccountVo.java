package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

public class PublicStoreAccountVo {
    private Long id;

    private String fundsSources;

    private String loanBelong;

    private String accountName;

    private String accountBank;

    private String accountNum;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank == null ? null : accountBank.trim();
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum == null ? null : accountNum.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}