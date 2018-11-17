package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

public class SpecialTradeRelation extends BaseDomain {
    private static final long serialVersionUID = 8492096759774895856L;

    private Long id;

    private Long effectiveId;

    private String tradeNo;

    private Date createTime;

    private Date updateTime;

    public Long getEffectiveId() {
        return effectiveId;
    }

    public void setEffectiveId(Long effectiveId) {
        this.effectiveId = effectiveId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
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