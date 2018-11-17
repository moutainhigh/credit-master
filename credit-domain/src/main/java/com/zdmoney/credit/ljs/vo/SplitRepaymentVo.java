package com.zdmoney.credit.ljs.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款分账信息vo
 * Created by YM10098 on 2017/6/8.
 */
public class SplitRepaymentVo {

    private Long loanId;
    private Long currentTerm;
    private BigDecimal capital; //本金
    private BigDecimal interest; //利息
    private Date tradeDate;//还款时间
    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }
}
