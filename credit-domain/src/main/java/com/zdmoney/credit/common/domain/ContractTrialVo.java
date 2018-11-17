package com.zdmoney.credit.common.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/11/16.
 */
public class ContractTrialVo {

    private BigDecimal pactMoney;

    private Long time;

    private Long promiseReturnDate;

    private Date startrdate;

    private Date endrdate;

    private BigDecimal referRate;

    private BigDecimal evalRate;

    private BigDecimal manageRate;

    private BigDecimal risk;

    private BigDecimal manageRateForPartyC;

    private BigDecimal rateSum;

    private BigDecimal rateem;

    private BigDecimal rateey;

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getPromiseReturnDate() {
        return promiseReturnDate;
    }

    public void setPromiseReturnDate(Long promiseReturnDate) {
        this.promiseReturnDate = promiseReturnDate;
    }

    public Date getStartrdate() {
        return startrdate;
    }

    public void setStartrdate(Date startrdate) {
        this.startrdate = startrdate;
    }

    public Date getEndrdate() {
        return endrdate;
    }

    public void setEndrdate(Date endrdate) {
        this.endrdate = endrdate;
    }

    public BigDecimal getReferRate() {
        return referRate;
    }

    public void setReferRate(BigDecimal referRate) {
        this.referRate = referRate;
    }

    public BigDecimal getEvalRate() {
        return evalRate;
    }

    public void setEvalRate(BigDecimal evalRate) {
        this.evalRate = evalRate;
    }

    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    public BigDecimal getRisk() {
        return risk;
    }

    public void setRisk(BigDecimal risk) {
        this.risk = risk;
    }

    public BigDecimal getManageRateForPartyC() {
        return manageRateForPartyC;
    }

    public void setManageRateForPartyC(BigDecimal manageRateForPartyC) {
        this.manageRateForPartyC = manageRateForPartyC;
    }

    public BigDecimal getRateSum() {
        return rateSum;
    }

    public void setRateSum(BigDecimal rateSum) {
        this.rateSum = rateSum;
    }

    public BigDecimal getRateem() {
        return rateem;
    }

    public void setRateem(BigDecimal rateem) {
        this.rateem = rateem;
    }

    public BigDecimal getRateey() {
        return rateey;
    }

    public void setRateey(BigDecimal rateey) {
        this.rateey = rateey;
    }
}
