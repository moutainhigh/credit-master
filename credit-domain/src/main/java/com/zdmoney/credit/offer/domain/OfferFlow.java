package com.zdmoney.credit.offer.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OfferFlow extends BaseDomain{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8010611323390367493L;

	private Long id;

    private String account;

    private String acctTitle;

    private String appoAcct;

    private String appoAcctTitle;

    private String appoDorc;

    private String delegationTeller;

    private String dorc;

    private String memo;

    private String memo2;

    private String memo3;

    private String organ;

    private String reversedNo;

    private String teller;

    private BigDecimal tradeAmount;

    private String tradeCode;

    private Date tradeDate;

    private String tradeKind;

    private String tradeNo;

    private String tradeType;

    private String voucherCode;

    private String voucherKind;

    private BigDecimal endAmount;

    private Long repayInfoId;

    private Long loanId;
    
    private Date createTime;

    private Date updateTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

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

    public String getDelegationTeller() {
        return delegationTeller;
    }

    public void setDelegationTeller(String delegationTeller) {
        this.delegationTeller = delegationTeller == null ? null : delegationTeller.trim();
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

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ == null ? null : organ.trim();
    }

    public String getReversedNo() {
        return reversedNo;
    }

    public void setReversedNo(String reversedNo) {
        this.reversedNo = reversedNo == null ? null : reversedNo.trim();
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller == null ? null : teller.trim();
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode == null ? null : voucherCode.trim();
    }


    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRepayInfoId() {
		return repayInfoId;
	}

	public void setRepayInfoId(Long repayInfoId) {
		this.repayInfoId = repayInfoId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
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

	public String getVoucherKind() {
		return voucherKind;
	}

	public void setVoucherKind(String voucherKind) {
		this.voucherKind = voucherKind;
	}

}