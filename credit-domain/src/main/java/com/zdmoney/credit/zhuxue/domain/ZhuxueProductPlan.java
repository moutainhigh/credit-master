package com.zdmoney.credit.zhuxue.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ZhuxueProductPlan extends BaseDomain {
    
	private static final long serialVersionUID = 1L;

	private Long id;

    private String code;

    private Date endDate;

    private BigDecimal interestRate;

    private BigDecimal margin;

    private String name;

    private BigDecimal orgFeeRatio;

    private Long orgRepayTerm;

    private Long organizationId;

    private BigDecimal pactMoney;

    private Long productId;

    private BigDecimal rateSum;

    private BigDecimal requestMoney;

    private Date startDate;

    private BigDecimal term;

    private Long operatorId;

    private BigDecimal actualRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getOrgFeeRatio() {
		return orgFeeRatio;
	}

	public void setOrgFeeRatio(BigDecimal orgFeeRatio) {
		this.orgFeeRatio = orgFeeRatio;
	}

	public Long getOrgRepayTerm() {
		return orgRepayTerm;
	}

	public void setOrgRepayTerm(Long orgRepayTerm) {
		this.orgRepayTerm = orgRepayTerm;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getRateSum() {
		return rateSum;
	}

	public void setRateSum(BigDecimal rateSum) {
		this.rateSum = rateSum;
	}

	public BigDecimal getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(BigDecimal requestMoney) {
		this.requestMoney = requestMoney;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getTerm() {
		return term;
	}

	public void setTerm(BigDecimal term) {
		this.term = term;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public BigDecimal getActualRate() {
		return actualRate;
	}

	public void setActualRate(BigDecimal actualRate) {
		this.actualRate = actualRate;
	}

}