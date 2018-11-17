package com.zdmoney.credit.loan.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanPreSettle extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3770868599306295646L;

	private Long id;

    private Long loanId;

    private Date settleDate;

    private Date createTime;

    private Date updateTime;

    private Date realSettleDate;

    public Date getRealSettleDate() {
        return realSettleDate;
    }

    public void setRealSettleDate(Date realSettleDate) {
        this.realSettleDate = realSettleDate;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
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