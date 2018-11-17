package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SpecialRepaymentRelation extends BaseDomain {
    private static final long serialVersionUID = 8498596859774895856L;

    private Long id;

    private Long applyId;

    private Long effectiveId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getEffectiveId() {
        return effectiveId;
    }

    public void setEffectiveId(Long effectiveId) {
        this.effectiveId = effectiveId;
    }
}