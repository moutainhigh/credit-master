package com.zdmoney.credit.ljs.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanStatusLufax extends BaseDomain{
	
	
	private static final long serialVersionUID = 2595847199389757641L;

	private Long id;

    private Long loanId;

    private String loanStatus;

    private Date createTime;

    private Date updateTime;

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

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus == null ? null : loanStatus.trim();
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
}