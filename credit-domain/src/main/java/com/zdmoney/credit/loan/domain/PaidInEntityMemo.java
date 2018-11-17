package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.vo.abs.entity.PaidInEntity;


@SuppressWarnings("all")
public class PaidInEntityMemo extends PaidInEntity {
    
    private static final long serialVersionUID = 1856435841684751462L;

    private String id;
    
    private String loanId;
    
    private String batNo;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }
    
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
    
    public String getBatNo() {
        return batNo;
    }

    public void setBatNo(String batNo) {
        this.batNo = batNo;
    }

}
