package com.zdmoney.credit.common.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/11/15.
 */
public class ReturnLoanContractTrialVo {

    private Long currentTerm;

    private BigDecimal currentAccrual;

    private BigDecimal giveBackRate;

    private BigDecimal principalBalance;

    private BigDecimal repaymentAll;

    private Date returnDate;

    private Date penaltyDate;

    private BigDecimal returneterm;

    private BigDecimal penalty;

    public Long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public BigDecimal getCurrentAccrual() {
        return currentAccrual;
    }

    public void setCurrentAccrual(BigDecimal currentAccrual) {
        this.currentAccrual = currentAccrual;
    }

    public BigDecimal getGiveBackRate() {
        return giveBackRate;
    }

    public void setGiveBackRate(BigDecimal giveBackRate) {
        this.giveBackRate = giveBackRate;
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getRepaymentAll() {
        return repaymentAll;
    }

    public void setRepaymentAll(BigDecimal repaymentAll) {
        this.repaymentAll = repaymentAll;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyDate(Date penaltyDate) {
        this.penaltyDate = penaltyDate;
    }

    public BigDecimal getReturneterm() {
        return returneterm;
    }

    public void setReturneterm(BigDecimal returneterm) {
        this.returneterm = returneterm;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
}
