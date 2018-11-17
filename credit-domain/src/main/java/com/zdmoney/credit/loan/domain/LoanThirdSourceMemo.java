package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanThirdSourceMemo extends BaseDomain {
    
	private static final long serialVersionUID = -20796437828164843L;

	private Long id;

    private Long offerRepayInfoId;

    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOfferRepayInfoId() {
        return offerRepayInfoId;
    }

    public void setOfferRepayInfoId(Long offerRepayInfoId) {
        this.offerRepayInfoId = offerRepayInfoId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}