package com.zdmoney.credit.loan.vo;

import com.zdmoney.credit.common.constant.SpecialReliefTypeEnum;

import java.util.Date;

/**
 * Created by ym10094 on 2017/11/1.
 */
public class MaxReliefAmountParamVo {

    private Long loanId;

    private Date tradeDate;

    private String reliefType;//减免类型

    private String isPecial = SpecialReliefTypeEnum.非特殊减免.getCode();//是否特殊 默认非特殊

    public String getIsPecial() {
        return isPecial;
    }

    public void setIsPecial(String isPecial) {
        this.isPecial = isPecial;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getReliefType() {
        return reliefType;
    }

    public void setReliefType(String reliefType) {
        this.reliefType = reliefType;
    }
}
