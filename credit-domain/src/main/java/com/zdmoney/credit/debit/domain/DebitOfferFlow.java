package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class DebitOfferFlow extends BaseDomain{
	
	
	private static final long serialVersionUID = 4146034705108560752L;

	private Long id;

    private String batNo;

    private Date tradeDate;

    private Long loanId;

    private String pactNo;

    private String serialNo;

    private String acNo;

    private String cardChn;

    private String repyType;

    private BigDecimal repayAmt;

    private Integer cnt;

    private String subjType;

    private BigDecimal subjAmt;

    private String state;

    private String memo;

    private Date createTime;

    private String creator;

    private Date updateTime;

    private String updator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatNo() {
        return batNo;
    }

    public void setBatNo(String batNo) {
        this.batNo = batNo == null ? null : batNo.trim();
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getPactNo() {
        return pactNo;
    }

    public void setPactNo(String pactNo) {
        this.pactNo = pactNo == null ? null : pactNo.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo == null ? null : acNo.trim();
    }

    public String getCardChn() {
        return cardChn;
    }

    public void setCardChn(String cardChn) {
        this.cardChn = cardChn == null ? null : cardChn.trim();
    }

    public String getRepyType() {
        return repyType;
    }

    public void setRepyType(String repyType) {
        this.repyType = repyType == null ? null : repyType.trim();
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getSubjType() {
        return subjType;
    }

    public void setSubjType(String subjType) {
        this.subjType = subjType == null ? null : subjType.trim();
    }

    public BigDecimal getSubjAmt() {
        return subjAmt;
    }

    public void setSubjAmt(BigDecimal subjAmt) {
        this.subjAmt = subjAmt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }
}