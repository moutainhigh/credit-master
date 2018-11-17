package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanFeeRepayRecord extends BaseDomain {
    private Long id;

    private Long loanId;

    private Long feeId;

    private String acctTitle;

    private BigDecimal amount;

    private BigDecimal repayamount;

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

    public Long getfeeId() {
        return feeId;
    }

    public void setfeeId(Long feeId) {
        this.feeId = feeId;
    }

    public String getAcctTitle() {
        return acctTitle;
    }

    public void setAcctTitle(String acctTitle) {
        this.acctTitle = acctTitle == null ? null : acctTitle.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRepayamount() {
        return repayamount;
    }

    public void setRepayamount(BigDecimal repayamount) {
        this.repayamount = repayamount;
    }
}