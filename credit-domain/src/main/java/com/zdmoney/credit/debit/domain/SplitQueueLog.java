package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SplitQueueLog extends BaseDomain{
   
	private static final long serialVersionUID = 4027976743094203814L;

	private Long id;

    private Long loanId;

    private String tradeNo;

    private String splitNotifyState;

    private String splitResultState;

    private String payOffType;

    private String splitNo;

    private Date splitNotifyDate;

    private Date splitResultDate;

    private String batchId;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;
    
    private String debitNo;

    private BigDecimal frozenAmount;
    
    private String sendEntrustFlag;
    
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getSplitNotifyState() {
        return splitNotifyState;
    }

    public void setSplitNotifyState(String splitNotifyState) {
        this.splitNotifyState = splitNotifyState == null ? null : splitNotifyState.trim();
    }

    public String getSplitResultState() {
        return splitResultState;
    }

    public void setSplitResultState(String splitResultState) {
        this.splitResultState = splitResultState == null ? null : splitResultState.trim();
    }

    public String getPayOffType() {
        return payOffType;
    }

    public void setPayOffType(String payOffType) {
        this.payOffType = payOffType == null ? null : payOffType.trim();
    }

    public String getSplitNo() {
        return splitNo;
    }

    public void setSplitNo(String splitNo) {
        this.splitNo = splitNo == null ? null : splitNo.trim();
    }

    public Date getSplitNotifyDate() {
        return splitNotifyDate;
    }

    public void setSplitNotifyDate(Date splitNotifyDate) {
        this.splitNotifyDate = splitNotifyDate;
    }

    public Date getSplitResultDate() {
        return splitResultDate;
    }

    public void setSplitResultDate(Date splitResultDate) {
        this.splitResultDate = splitResultDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getDebitNo() {
		return debitNo;
	}

	public void setDebitNo(String debitNo) {
		this.debitNo = debitNo;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getSendEntrustFlag() {
		return sendEntrustFlag;
	}

	public void setSendEntrustFlag(String sendEntrustFlag) {
		this.sendEntrustFlag = sendEntrustFlag;
	}
    
}