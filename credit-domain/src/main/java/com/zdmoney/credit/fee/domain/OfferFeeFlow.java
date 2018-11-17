package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OfferFeeFlow extends BaseDomain{
    private Long id;

    private String acctTitle;

    private String appoAcct;

    private String appoAcctTitle;

    private String appoDorc;

    private String dorc;

    private String memo;

    private String memo2;

    private String memo3;

    private BigDecimal tradeAmount;

    private String tradeCode;

    private Date tradeDate;

    private String tradeKind;

    private String serialNo;

    private String tradeType;

    private Long loanId;

    private Long feeId;

    private Date createTime;

    private Date updateTime;



    public String getAcctTitle() {
        return acctTitle;
    }

    public void setAcctTitle(String acctTitle) {
        this.acctTitle = acctTitle == null ? null : acctTitle.trim();
    }

    public String getAppoAcct() {
        return appoAcct;
    }

    public void setAppoAcct(String appoAcct) {
        this.appoAcct = appoAcct == null ? null : appoAcct.trim();
    }

    public String getAppoAcctTitle() {
        return appoAcctTitle;
    }

    public void setAppoAcctTitle(String appoAcctTitle) {
        this.appoAcctTitle = appoAcctTitle == null ? null : appoAcctTitle.trim();
    }

    public String getAppoDorc() {
        return appoDorc;
    }

    public void setAppoDorc(String appoDorc) {
        this.appoDorc = appoDorc == null ? null : appoDorc.trim();
    }

    public String getDorc() {
        return dorc;
    }

    public void setDorc(String dorc) {
        this.dorc = dorc == null ? null : dorc.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2 == null ? null : memo2.trim();
    }

    public String getMemo3() {
        return memo3;
    }

    public void setMemo3(String memo3) {
        this.memo3 = memo3 == null ? null : memo3.trim();
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode == null ? null : tradeCode.trim();
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeKind() {
        return tradeKind;
    }

    public void setTradeKind(String tradeKind) {
        this.tradeKind = tradeKind == null ? null : tradeKind.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

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

	public Long getfeeId() {
		return feeId;
	}

	public void setfeeId(Long feeId) {
		this.feeId = feeId;
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