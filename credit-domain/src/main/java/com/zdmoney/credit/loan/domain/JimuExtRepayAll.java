package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class JimuExtRepayAll extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2608673111444085294L;

	private Long id;

    private BigDecimal accrualRevise;

    private Integer currentTerm;

    private Date doneRepayAllFactTime;

    private BigDecimal faxi;

    private BigDecimal jianmian;

    private Long loanId;

    private Date notifyDate;

    private Date promiseRepayDate;

    private BigDecimal promiseReturneterm;

    private BigDecimal repayAll;

    private Date tradeDate;

    private BigDecimal tuifei;

    private BigDecimal weiyuejing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAccrualRevise() {
        return accrualRevise;
    }

    public void setAccrualRevise(BigDecimal accrualRevise) {
        this.accrualRevise = accrualRevise;
    }

    public Integer getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Integer currentTerm) {
        this.currentTerm = currentTerm;
    }

    public Date getDoneRepayAllFactTime() {
        return doneRepayAllFactTime;
    }

    public void setDoneRepayAllFactTime(Date doneRepayAllFactTime) {
        this.doneRepayAllFactTime = doneRepayAllFactTime;
    }

    public BigDecimal getFaxi() {
        return faxi;
    }

    public void setFaxi(BigDecimal faxi) {
        this.faxi = faxi;
    }

    public BigDecimal getJianmian() {
        return jianmian;
    }

    public void setJianmian(BigDecimal jianmian) {
        this.jianmian = jianmian;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Date getPromiseRepayDate() {
        return promiseRepayDate;
    }

    public void setPromiseRepayDate(Date promiseRepayDate) {
        this.promiseRepayDate = promiseRepayDate;
    }

    public BigDecimal getPromiseReturneterm() {
        return promiseReturneterm;
    }

    public void setPromiseReturneterm(BigDecimal promiseReturneterm) {
        this.promiseReturneterm = promiseReturneterm;
    }

    public BigDecimal getRepayAll() {
        return repayAll;
    }

    public void setRepayAll(BigDecimal repayAll) {
        this.repayAll = repayAll;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getTuifei() {
        return tuifei;
    }

    public void setTuifei(BigDecimal tuifei) {
        this.tuifei = tuifei;
    }

    public BigDecimal getWeiyuejing() {
        return weiyuejing;
    }

    public void setWeiyuejing(BigDecimal weiyuejing) {
        this.weiyuejing = weiyuejing;
    }

}