package com.zdmoney.credit.loan.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanOverdueHistory extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4156140556980014916L;

	private Long id;

    private Long overdueDay;

    private Short overdueTime;

    private Long overdueDayHisBig;

    private Short overdueTimeHisBig;

    private Long loanId;

    private Date createTime;

    private Date updateTime;

    private Integer residualTime;

    private BigDecimal lastRepaymentAmount;

    private Date lastRepaymentDate;

   

    public Long getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(Long overdueDay) {
        this.overdueDay = overdueDay;
    }

    public Short getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(Short overdueTime) {
        this.overdueTime = overdueTime;
    }

    public Long getOverdueDayHisBig() {
        return overdueDayHisBig;
    }

    public void setOverdueDayHisBig(Long overdueDayHisBig) {
        this.overdueDayHisBig = overdueDayHisBig;
    }

    public Short getOverdueTimeHisBig() {
        return overdueTimeHisBig;
    }

    public void setOverdueTimeHisBig(Short overdueTimeHisBig) {
        this.overdueTimeHisBig = overdueTimeHisBig;
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

    public Integer getResidualTime() {
        return residualTime;
    }

    public void setResidualTime(Integer residualTime) {
        this.residualTime = residualTime;
    }

    public BigDecimal getLastRepaymentAmount() {
        return lastRepaymentAmount;
    }

    public void setLastRepaymentAmount(BigDecimal lastRepaymentAmount) {
        this.lastRepaymentAmount = lastRepaymentAmount;
    }

    public Date getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    public void setLastRepaymentDate(Date lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
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