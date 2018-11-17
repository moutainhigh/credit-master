package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class JimuProduct extends BaseDomain {
	private static final long serialVersionUID = 1L;

	private Long id;

    private BigDecimal managerRateForPartyc;

    private BigDecimal rateey;

    private Long time;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getManagerRateForPartyc() {
        return managerRateForPartyc;
    }

    public void setManagerRateForPartyc(BigDecimal managerRateForPartyc) {
        this.managerRateForPartyc = managerRateForPartyc;
    }

    public BigDecimal getRateey() {
        return rateey;
    }

    public void setRateey(BigDecimal rateey) {
        this.rateey = rateey;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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