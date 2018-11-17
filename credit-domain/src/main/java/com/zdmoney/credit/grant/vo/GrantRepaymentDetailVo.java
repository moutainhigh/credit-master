package com.zdmoney.credit.grant.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ym10094 on 2016/11/11.还款计划
 */
public class GrantRepaymentDetailVo implements Serializable {
    private static final long serialVersionUID = 3873417642726789126L;
    /**
     * 当期利息
     */
    private BigDecimal currentAccrual = new BigDecimal("0.00");
    /**
     * 当前期数
     */
    private Integer currentTerm;
    /**
     *本金余额
     */
    private BigDecimal principalBalance = new BigDecimal("0.00");
    /**
     *一次性还款金额
     */
    private BigDecimal repaymentAll = new BigDecimal("0.00");
    /**
     *还款日期 YYYYMMDD
     */
    private String returnDate;
    /**
     *剩余欠款,用于记录不足额部分
     */
    private BigDecimal deficit = new BigDecimal("0.00");
    /**
     *当前还款状态
     */
    private String repaymentState;
    /**
     *结清日期 YYYYMMDD
     */
    private String factreturnDate;
    /**
     *罚息起算日期 YYYYMMDD
     */
    private String penaltyDate;
    /**
     *每期还款金额
     */
    private BigDecimal returneterm = new BigDecimal("0.00");

    /** 本期次账单日YYYYMMDD **/
    private String endDate;
    
    /** 当期本金 **/
    private BigDecimal prcpAmt;

    public BigDecimal getCurrentAccrual() {
        return currentAccrual;
    }

    public void setCurrentAccrual(BigDecimal currentAccrual) {
        this.currentAccrual = currentAccrual;
    }

    public Integer getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Integer currentTerm) {
        this.currentTerm = currentTerm;
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getDeficit() {
        return deficit;
    }

    public void setDeficit(BigDecimal deficit) {
        this.deficit = deficit;
    }

    public String getRepaymentState() {
        return repaymentState;
    }

    public void setRepaymentState(String repaymentState) {
        this.repaymentState = repaymentState;
    }

    public String getFactreturnDate() {
        return factreturnDate;
    }

    public void setFactreturnDate(String factreturnDate) {
        this.factreturnDate = factreturnDate;
    }

    public String getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyDate(String penaltyDate) {
        this.penaltyDate = penaltyDate;
    }

    public BigDecimal getReturneterm() {
        return returneterm;
    }

    public void setReturneterm(BigDecimal returneterm) {
        this.returneterm = returneterm;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrcpAmt() {
        return prcpAmt;
    }

    public void setPrcpAmt(BigDecimal prcpAmt) {
        this.prcpAmt = prcpAmt;
    }
}
