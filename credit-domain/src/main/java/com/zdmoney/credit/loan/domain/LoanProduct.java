package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanProduct extends BaseDomain {

	private static final long serialVersionUID = 1L;

	private Long id;

    private Long loanId;

    private Date startrdate;

    private Date endrdate;

    private BigDecimal evalRate;

    private BigDecimal manageRate;

    private BigDecimal pactMoney;

    private Long promiseReturnDate;

    private BigDecimal rateem;

    private BigDecimal rateey;
    
    private BigDecimal rateed;

    private BigDecimal rateSum;

    private BigDecimal referRate;

    private Long time;

    private BigDecimal grantMoney;

    private BigDecimal margin;

    private BigDecimal risk;

    private BigDecimal penaltyRate;

    private Date createTime;

    private Date updateTime;

    private BigDecimal residualPactMoney;

    private BigDecimal manageRateForPartyC;

    private BigDecimal accrualem;

    private BigDecimal advance;

    private BigDecimal rate;
    
    private String calculatorType;

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

	public Date getStartrdate() {
		return startrdate;
	}

	public void setStartrdate(Date startrdate) {
		this.startrdate = startrdate;
	}

	public Date getEndrdate() {
		return endrdate;
	}

	public void setEndrdate(Date endrdate) {
		this.endrdate = endrdate;
	}

	public BigDecimal getEvalRate() {
		return evalRate;
	}

	public void setEvalRate(BigDecimal evalRate) {
		this.evalRate = evalRate;
	}

	public BigDecimal getManageRate() {
		return manageRate;
	}

	public void setManageRate(BigDecimal manageRate) {
		this.manageRate = manageRate;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}


	public BigDecimal getRateem() {
		return rateem;
	}

	public void setRateem(BigDecimal rateem) {
		this.rateem = rateem;
	}

	public BigDecimal getRateey() {
		return rateey;
	}

	public void setRateey(BigDecimal rateey) {
		this.rateey = rateey;
	}

	public BigDecimal getRateSum() {
		return rateSum;
	}

	public void setRateSum(BigDecimal rateSum) {
		this.rateSum = rateSum;
	}

	public BigDecimal getReferRate() {
		return referRate;
	}

	public void setReferRate(BigDecimal referRate) {
		this.referRate = referRate;
	}

	public BigDecimal getGrantMoney() {
		return grantMoney;
	}

	public void setGrantMoney(BigDecimal grantMoney) {
		this.grantMoney = grantMoney;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public BigDecimal getRisk() {
		return risk;
	}

	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}

	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}

	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
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

	public BigDecimal getResidualPactMoney() {
		if(residualPactMoney==null){
			residualPactMoney=BigDecimal.ZERO;
		}
		if(residualPactMoney.equals(null)){
			residualPactMoney=BigDecimal.ZERO;
		}
		return residualPactMoney;
	}

	public void setResidualPactMoney(BigDecimal residualPactMoney) {
		this.residualPactMoney = residualPactMoney;
	}

	public BigDecimal getManageRateForPartyC() {
		return manageRateForPartyC;
	}

	public void setManageRateForPartyC(BigDecimal manageRateForPartyC) {
		this.manageRateForPartyC = manageRateForPartyC;
	}

	public BigDecimal getAccrualem() {
		return accrualem;
	}

	public void setAccrualem(BigDecimal accrualem) {
		this.accrualem = accrualem;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Long getPromiseReturnDate() {
		return promiseReturnDate;
	}

	public void setPromiseReturnDate(Long promiseReturnDate) {
		this.promiseReturnDate = promiseReturnDate;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	public String getCalculatorType() {
        return calculatorType;
    }

    public void setCalculatorType(String calculatorType) {
        this.calculatorType = calculatorType == null ? null : calculatorType.trim();
    }

	public BigDecimal getRateed() {
		return rateed;
	}

	public void setRateed(BigDecimal rateed) {
		this.rateed = rateed;
	}
	
    
}
