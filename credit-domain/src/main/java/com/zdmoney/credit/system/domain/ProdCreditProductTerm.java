package com.zdmoney.credit.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ProdCreditProductTerm extends BaseDomain{
    private Long id;

    private Long productId;

    private Long term;

    private Long lowerLimit;

    private Long upperLimit;

    private Date createTime;

    private Date updateTime;

    private String operator;
    
    private String productName;
    
    //实际到账利率（综合费率）
    private BigDecimal rate;
    
    //银行年利率均化到每月的利率 ，即平均每月利息（补偿利率）
    private BigDecimal accrualem;
    
	//罚息利率
    private BigDecimal penaltyRate;
    
    //优惠综合费率
    private BigDecimal reloanRate;
    
    //优惠补偿利率
    private BigDecimal reloanAccrualem;
    
    public BigDecimal getReloanAccrualem() {
		return reloanAccrualem;
	}

	public void setReloanAccrualem(BigDecimal reloanAccrualem) {
		this.reloanAccrualem = reloanAccrualem;
	}

	public BigDecimal getReloanRate() {
		return reloanRate;
	}

	public void setReloanRate(BigDecimal reloanRate) {
		this.reloanRate = reloanRate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAccrualem() {
		return accrualem;
	}

	public void setAccrualem(BigDecimal accrualem) {
		this.accrualem = accrualem;
	}

	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}

	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
	}

    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Long getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Long upperLimit) {
        this.upperLimit = upperLimit;
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