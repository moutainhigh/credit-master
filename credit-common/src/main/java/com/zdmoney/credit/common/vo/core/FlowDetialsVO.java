package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

/**
 * Created by 刘必成 on 15-4-7.
 */
public class FlowDetialsVO {

    String acctTitle;           //明细项目
    String acctTitleName;      //明细项目名称
    String time;                //期数
    BigDecimal tradeAmount;    //明细金额

    public String getAcctTitle() {
        return acctTitle;
    }

    public void setAcctTitle(String acctTitle) {
        this.acctTitle = acctTitle;
    }
    
    public String getAcctTitleName() { 
    	return acctTitleName;
    }

    public void setAcctTitleName(String acctTitleName) {
        this.acctTitleName = acctTitleName;
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
}
