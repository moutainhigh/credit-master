package com.zdmoney.credit.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ProdCreditProductInfo extends BaseDomain{
    private Long id;

    private BigDecimal accrualem;

    private String loanType;

    private BigDecimal rate;

    private String category;

    private String code;

    private Date startDate;

    private Date endDate;

    private BigDecimal penaltyRate;

    private String hasPlan;

    private Date createTime;

    private Date updateTime;

    private String operator;
    
    private ProdCreditProductTerm term;
    

    public ProdCreditProductTerm getTerm() {
		return term;
	}

	public void setTerm(ProdCreditProductTerm term) {
		this.term = term;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getAccrualem() {
        return accrualem;
    }

    public void setAccrualem(BigDecimal accrualem) {
        this.accrualem = accrualem;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }


    public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public String getHasPlan() {
        return hasPlan;
    }

    public void setHasPlan(String hasPlan) {
        this.hasPlan = hasPlan == null ? null : hasPlan.trim();
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }
}