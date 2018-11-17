package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class DebitQueueLog extends BaseDomain {

    private static final long serialVersionUID = -4225525590559790981L;

    private Long id;

    private Long loanId;

    private String tradeNo;

    private String debitNotifyState;

    private String debitResultState;

    private String debitType;

    private String debitNo;

    private Date debitNotifyDate;

    private Date debitResultDate;

    private String batchId;

    private BigDecimal amount;

    private String payParty;

    private String repayType;

    private Long debitTransactionId;

    private String memo;
    
    private Long repayTerm;

    public DebitQueueLog() {

    }

    public DebitQueueLog(String loanId, BigDecimal amount, String debitNo, String batchId) {
        this.loanId = Long.valueOf(loanId);
        this.amount = amount;
        this.debitNo = debitNo;
        this.batchId = batchId;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getDebitNotifyState() {
        return debitNotifyState;
    }

    public void setDebitNotifyState(String debitNotifyState) {
        this.debitNotifyState = debitNotifyState == null ? null
                : debitNotifyState.trim();
    }

    public String getDebitResultState() {
        return debitResultState;
    }

    public void setDebitResultState(String debitResultState) {
        this.debitResultState = debitResultState == null ? null
                : debitResultState.trim();
    }

    public String getDebitType() {
        return debitType;
    }

    public void setDebitType(String debitType) {
        this.debitType = debitType == null ? null : debitType.trim();
    }

    public String getDebitNo() {
        return debitNo;
    }

    public void setDebitNo(String debitNo) {
        this.debitNo = debitNo == null ? null : debitNo.trim();
    }

    public Date getDebitNotifyDate() {
        return debitNotifyDate;
    }

    public void setDebitNotifyDate(Date debitNotifyDate) {
        this.debitNotifyDate = debitNotifyDate;
    }

    public Date getDebitResultDate() {
        return debitResultDate;
    }

    public void setDebitResultDate(Date debitResultDate) {
        this.debitResultDate = debitResultDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayParty() {
        return payParty;
    }

    public void setPayParty(String payParty) {
        this.payParty = payParty;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public Long getDebitTransactionId() {
        return debitTransactionId;
    }

    public void setDebitTransactionId(Long debitTransactionId) {
        this.debitTransactionId = debitTransactionId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

	public Long getRepayTerm() {
		return repayTerm;
	}

	public void setRepayTerm(Long repayTerm) {
		this.repayTerm = repayTerm;
	}
}