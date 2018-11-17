package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanRepaymentLevelHis extends BaseDomain {

	private static final long serialVersionUID = 1L;

	private BigDecimal id;

	private Long loanId;

	private String currentTerm;

	private String repayLevel;
	
	private String accountClassificationForWebpage;
	
	private String accountClassificationForReportingSystems;
	
	private String customerLevel;

	private Date createTime;

	private Date updateTime;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(String currentTerm) {
		this.currentTerm = currentTerm;
	}

	public String getRepayLevel() {
		return repayLevel;
	}

	public void setRepayLevel(String repayLevel) {
		this.repayLevel = repayLevel;
	}
	

	public String getAccountClassificationForWebpage() {
        return accountClassificationForWebpage;
    }

    public void setAccountClassificationForWebpage(
            String accountClassificationForWebpage) {
        this.accountClassificationForWebpage = accountClassificationForWebpage;
    }

    public String getAccountClassificationForReportingSystems() {
        return accountClassificationForReportingSystems;
    }

    public void setAccountClassificationForReportingSystems(
            String accountClassificationForReportingSystems) {
        this.accountClassificationForReportingSystems = accountClassificationForReportingSystems;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
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
