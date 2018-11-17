package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReturnAccountCardVO {

    Date tradeDate;                     //交易日期，格式为yyyy-MM-dd，例如：2015-03-13。
    String tradeType;                   //交易方式, TradeType{现金,转账,通联代扣,富友代扣,上海银联代扣,冲正补记,冲正,挂账,保证金,系统使用保证金,风险金},详见数据字典.
    String tradeCode;                   //交易类型
    String tradeName;                   //交易类型名称
    BigDecimal beginBalance;           //期初余额
    BigDecimal income;                  //收入
    BigDecimal outlay;                  //支出
    BigDecimal endBalance;             //期末余额
    String memo;                        //备注
    String tradeNo;                    //流水号
    List<FlowDetialsVO> flowDetialsVOs;//流水明细信息列表

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }
    
    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public BigDecimal getBeginBalance() {
        return beginBalance;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutlay() {
        return outlay;
    }

    public void setOutlay(BigDecimal outlay) {
        this.outlay = outlay;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public List<FlowDetialsVO> getFlowDetialsVOs() {
        return flowDetialsVOs;
    }

    public void setFlowDetialsVO(List<FlowDetialsVO> flowDetialsVOs) {
        this.flowDetialsVOs = flowDetialsVOs;
    }
}
