package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanBankExt extends BaseDomain {
    
    private static final long serialVersionUID = -2783463543439217823L;

    /** 主键ID **/
    private Long id;

    /** LOAN_BANK表主键 **/
    private Long loanBankId;

    /** OFF_LINE_OFFER_BANK_DIC表主键 **/
    private Long offerBankId;

    /** 备注 **/
    private String memo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanBankId() {
        return loanBankId;
    }

    public void setLoanBankId(Long loanBankId) {
        this.loanBankId = loanBankId;
    }

    public Long getOfferBankId() {
        return offerBankId;
    }

    public void setOfferBankId(Long offerBankId) {
        this.offerBankId = offerBankId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}