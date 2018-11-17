package com.zdmoney.credit.loan.vo;

import com.zdmoney.credit.common.vo.core.TrailVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/5/16.
 */
public class ReliefAmountCalculateVo{
    /**
     * 对应累计已经金额
     */
//    private BigDecimal alreadyRepayTotalMoney = new BigDecimal(0.0);
    /**
     * 月还款金额
     */
    private BigDecimal everyMonthRepayMoney = new BigDecimal(0.0);
    /**
     * 对应逾期应还金额
     */
    private BigDecimal overShouldRepayMoney = new BigDecimal(0.0);
    /**
     * 减免后应还金额
     */

//    private BigDecimal reliefAfterShouldRepayMoney = new BigDecimal(0.0);

    /**
     *应还金额
     */
    private BigDecimal shouldRepayMoney = new BigDecimal(0.0);
    /**
     * 实还金额
     */
    private BigDecimal realityMoney = new BigDecimal(0.0);
    /**
     * 历史对应累计已还
     */
    private BigDecimal historyAlreadyRepayTotalMoney = new BigDecimal(0.0);
    /**
     * 应还金额不包含当前
     */
//    private BigDecimal shouldRepayMoneyExcludeCurrentTerm = new BigDecimal(0.0);
    /**
     * 应还罚息
     */
    private BigDecimal fine = new BigDecimal(0.00);
    /**
     * 已收罚息
     */
    private BigDecimal alreadyPayFine = new BigDecimal(0.00);

/*    public BigDecimal getAlreadyRepayTotalMoney() {
        return alreadyRepayTotalMoney;
    }

    public void setAlreadyRepayTotalMoney(BigDecimal alreadyRepayTotalMoney) {
        this.alreadyRepayTotalMoney = alreadyRepayTotalMoney;
    }*/

    public BigDecimal getEveryMonthRepayMoney() {
        return everyMonthRepayMoney;
    }

    public void setEveryMonthRepayMoney(BigDecimal everyMonthRepayMoney) {
        this.everyMonthRepayMoney = everyMonthRepayMoney;
    }

    public BigDecimal getOverShouldRepayMoney() {
        return overShouldRepayMoney;
    }

    public void setOverShouldRepayMoney(BigDecimal overShouldRepayMoney) {
        this.overShouldRepayMoney = overShouldRepayMoney;
    }

/*    public BigDecimal getReliefAfterShouldRepayMoney() {
        return reliefAfterShouldRepayMoney;
    }

    public void setReliefAfterShouldRepayMoney(BigDecimal reliefAfterShouldRepayMoney) {
        this.reliefAfterShouldRepayMoney = reliefAfterShouldRepayMoney;
    }*/

    public BigDecimal getShouldRepayMoney() {
        return shouldRepayMoney;
    }

    public void setShouldRepayMoney(BigDecimal shouldRepayMoney) {
        this.shouldRepayMoney = shouldRepayMoney;
    }

    public BigDecimal getRealityMoney() {
        return realityMoney;
    }

    public void setRealityMoney(BigDecimal realityMoney) {
        this.realityMoney = realityMoney;
    }

    public BigDecimal getHistoryAlreadyRepayTotalMoney() {
        return historyAlreadyRepayTotalMoney;
    }

    public void setHistoryAlreadyRepayTotalMoney(BigDecimal historyAlreadyRepayTotalMoney) {
        this.historyAlreadyRepayTotalMoney = historyAlreadyRepayTotalMoney;
    }

/*    public BigDecimal getShouldRepayMoneyExcludeCurrentTerm() {
        return shouldRepayMoneyExcludeCurrentTerm;
    }

    public void setShouldRepayMoneyExcludeCurrentTerm(BigDecimal shouldRepayMoneyExcludeCurrentTerm) {
        this.shouldRepayMoneyExcludeCurrentTerm = shouldRepayMoneyExcludeCurrentTerm;
    }*/

    public BigDecimal getAlreadyPayFine() {
        return alreadyPayFine;
    }

    public void setAlreadyPayFine(BigDecimal alreadyPayFine) {
        this.alreadyPayFine = alreadyPayFine;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }
}
