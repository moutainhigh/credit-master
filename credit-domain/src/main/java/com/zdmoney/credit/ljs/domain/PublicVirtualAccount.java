package com.zdmoney.credit.ljs.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PublicVirtualAccount extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
    private Long id;

    private String accountType;

    private BigDecimal totalAmt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }
}