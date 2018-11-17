package com.zdmoney.credit.loan.vo;


import java.math.BigDecimal;
import java.util.Date;

public class LoanRepaymentDetailVo {

	private Long id;

    private String loanId;

    private BigDecimal currentAccrual;

    private Long currentTerm;

    private BigDecimal giveBackRate;

    private BigDecimal principalBalance;

    private BigDecimal repaymentAll;

    private Date returnDate;

    private BigDecimal deficit;

    private String repaymentState;

    private Date factReturnDate;

    private Date penaltyDate;

    private BigDecimal returneterm;

    private BigDecimal penalty;

    private BigDecimal accrualRevise;

    private Date createTime;

    private Date updateTime;

    private String contractNum;

    private String orgCode;
    
    private BigDecimal returnBalance;
    
    private String returnDateStr;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getCurrentAccrual() {
        return currentAccrual;
    }

    public void setCurrentAccrual(BigDecimal currentAccrual) {
        this.currentAccrual = currentAccrual;
    }


    public BigDecimal getGiveBackRate() {
        return giveBackRate;
    }

    public void setGiveBackRate(BigDecimal giveBackRate) {
        this.giveBackRate = giveBackRate;
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getRepaymentAll() {
        return repaymentAll;
    }

    public void setRepaymentAll(BigDecimal repaymentAll) {
        this.repaymentAll = repaymentAll;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getDeficit() {
        return deficit;
    }

    public void setDeficit(BigDecimal deficit) {
        this.deficit = deficit;
    }

    public String getRepaymentState() {
        return repaymentState;
    }

    public void setRepaymentState(String repaymentState) {
        this.repaymentState = repaymentState == null ? null : repaymentState.trim();
    }

    public Date getFactReturnDate() {
        return factReturnDate;
    }

    public void setFactReturnDate(Date factReturnDate) {
        this.factReturnDate = factReturnDate;
    }

    public Date getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyDate(Date penaltyDate) {
        this.penaltyDate = penaltyDate;
    }

    public BigDecimal getReturneterm() {
        return returneterm;
    }

    public void setReturneterm(BigDecimal returneterm) {
        this.returneterm = returneterm;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getAccrualRevise() {
        return accrualRevise;
    }

    public void setAccrualRevise(BigDecimal accrualRevise) {
        this.accrualRevise = accrualRevise;
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

	public Long getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public BigDecimal getReturnBalance() {
		return returnBalance;
	}

	public void setReturnBalance(BigDecimal returnBalance) {
		this.returnBalance = returnBalance;
	}

	public String getReturnDateStr() {
		return returnDateStr;
	}

	public void setReturnDateStr(String returnDateStr) {
		this.returnDateStr = returnDateStr;
	}
	
	
}