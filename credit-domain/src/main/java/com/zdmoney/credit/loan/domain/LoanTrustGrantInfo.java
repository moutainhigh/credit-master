package com.zdmoney.credit.loan.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanTrustGrantInfo extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7983580525964972232L;

	private Long id;

    private Long loanId;

    private String trustUserCode;

    private String creditUserCode;

    private BigDecimal amount;

    private BigDecimal income;

    private String tppReturnState;

    private String memo;

    private Date createTime;

    private Date updateTime;


    public String getTrustUserCode() {
        return trustUserCode;
    }

    public void setTrustUserCode(String trustUserCode) {
        this.trustUserCode = trustUserCode == null ? null : trustUserCode.trim();
    }

    public String getCreditUserCode() {
        return creditUserCode;
    }

    public void setCreditUserCode(String creditUserCode) {
        this.creditUserCode = creditUserCode == null ? null : creditUserCode.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getTppReturnState() {
        return tppReturnState;
    }

    public void setTppReturnState(String tppReturnState) {
        this.tppReturnState = tppReturnState == null ? null : tppReturnState.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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