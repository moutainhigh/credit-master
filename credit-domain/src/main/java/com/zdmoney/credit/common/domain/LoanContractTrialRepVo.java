package com.zdmoney.credit.common.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/11/16.
 * 借款合同试算接口  请求参数
 */
public class LoanContractTrialRepVo {
    /**贷款类型**/
    private String loanType;
    /**借款金额**/
    private BigDecimal money;
    /**借款期数**/
    private Long time;
    /**合同来源（渠道）**/
    private  String fundsSources;
    /**是否是费率优惠客户y，n**/
    private String isRatePreferLoan;

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources;
    }

    public String getIsRatePreferLoan() {
        return isRatePreferLoan;
    }

    public void setIsRatePreferLoan(String isRatePreferLoan) {
        this.isRatePreferLoan = isRatePreferLoan;
    }
}
