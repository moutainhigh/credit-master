package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class JimuExtRepay extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5286657852000900855L;

	private Long id;

    private BigDecimal accrualRevise;

    private Long currentTerm;

    private Date fillDate;

    private String hasFilled;

    private Date inAccountEndDay;

    private Long loanId;

    private BigDecimal notPaidUntilInAccEndDay;

    private BigDecimal paidBeforeInAccEndDay;

    private BigDecimal paidOnRepayDay;

    private Date promiseRepayDate;

    private BigDecimal promiseReturneterm;

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

    public Long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public Date getFillDate() {
        return fillDate;
    }

    public void setFillDate(Date fillDate) {
        this.fillDate = fillDate;
    }

    public String getHasFilled() {
        return hasFilled;
    }

    public void setHasFilled(String hasFilled) {
        this.hasFilled = hasFilled == null ? null : hasFilled.trim();
    }

    public Date getInAccountEndDay() {
        return inAccountEndDay;
    }

    public void setInAccountEndDay(Date inAccountEndDay) {
        this.inAccountEndDay = inAccountEndDay;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getNotPaidUntilInAccEndDay() {
        return notPaidUntilInAccEndDay;
    }

    public void setNotPaidUntilInAccEndDay(BigDecimal notPaidUntilInAccEndDay) {
        this.notPaidUntilInAccEndDay = notPaidUntilInAccEndDay;
    }

    public BigDecimal getPaidBeforeInAccEndDay() {
        return paidBeforeInAccEndDay;
    }

    public void setPaidBeforeInAccEndDay(BigDecimal paidBeforeInAccEndDay) {
        this.paidBeforeInAccEndDay = paidBeforeInAccEndDay;
    }

    public BigDecimal getPaidOnRepayDay() {
        return paidOnRepayDay;
    }

    public void setPaidOnRepayDay(BigDecimal paidOnRepayDay) {
        this.paidOnRepayDay = paidOnRepayDay;
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

}