package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/11/1.
 */
public class RelieCalculateAmountParamVo {
    private long loanId;
    private Date tradeDate;
    private BigDecimal repayAmount;
    private String isPecial;
    private String applyType;

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getIsPecial() {
        return isPecial;
    }

    public void setIsPecial(String isPecial) {
        this.isPecial = isPecial;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
}
