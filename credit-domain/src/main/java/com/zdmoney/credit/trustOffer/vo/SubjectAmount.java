package com.zdmoney.credit.trustOffer.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ym10094 on 2016/11/4.
 * 科目金额类
 */
public class SubjectAmount{
    /* 减免金额
     */
    public BigDecimal reliefAmount = new BigDecimal("0.00");
    /*罚息金额
     */
    public BigDecimal fineInterestAmount = new BigDecimal("0.00");
    /*违约金额
     */
    public BigDecimal penaltyAmount = new BigDecimal("0.00");
    /*利息金额
     */
    public BigDecimal interestAmount = new BigDecimal("0.00");
    /**
     * 本金金额
     */
    public BigDecimal principalAmount = new BigDecimal("0.00");
    /**
     * 已还总金额
     */
    public BigDecimal repayTotalAmount = new BigDecimal("0.00");
    /**
     * 专户总金额（渤海信托新增）
     */
    public BigDecimal specialAccountTotalAmount = new BigDecimal("0.00");

    public List<Long> terms;

    public BigDecimal getReliefAmount() {
        return reliefAmount;
    }

    public void setReliefAmount(BigDecimal reliefAmount) {
        this.reliefAmount = reliefAmount;
    }

    public BigDecimal getFineInterestAmount() {
        return fineInterestAmount;
    }

    public void setFineInterestAmount(BigDecimal fineInterestAmount) {
        this.fineInterestAmount = fineInterestAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public List<Long> getTerms() {
        return terms;
    }

    public void setTerms(List<Long> terms) {
        this.terms = terms;
    }

    public BigDecimal getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(BigDecimal repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public BigDecimal getSpecialAccountTotalAmount() {
        return specialAccountTotalAmount;
    }

    public void setSpecialAccountTotalAmount(BigDecimal specialAccountTotalAmount) {
        this.specialAccountTotalAmount = specialAccountTotalAmount;
    }
}
