package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanRepaymentState extends BaseDomain{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long loanId;

    private String reportMonth;

    private Long reportTerm;

    private Long currentTerm;

    private String state;

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

    public String getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth == null ? null : reportMonth.trim();
    }

    public Long getReportTerm() {
        return reportTerm;
    }

    public void setReportTerm(Long reportTerm) {
        this.reportTerm = reportTerm;
    }

    public Long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
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