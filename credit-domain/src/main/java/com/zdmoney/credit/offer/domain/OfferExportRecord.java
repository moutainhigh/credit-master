package com.zdmoney.credit.offer.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OfferExportRecord extends BaseDomain {
    
    private static final long serialVersionUID = 8793532654691045971L;

    private Long id;

    private Long transId;

    private Long offerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}