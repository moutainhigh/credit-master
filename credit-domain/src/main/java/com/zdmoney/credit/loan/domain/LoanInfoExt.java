package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanInfoExt extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2895089417823294773L;

	private Long id;

    private Long loanId;

    private String assignState;

    private String memo;

    private Date createTime;

    private String creator;

    private Date updateTime;

    private String updator;

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

    public String getAssignState() {
        return assignState;
    }

    public void setAssignState(String assignState) {
        this.assignState = assignState == null ? null : assignState.trim();
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }
}