package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 
 * 第三方划扣流水表
 * 
 * */
public class DebitTransaction extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 债权编号 */
    private Long loanId;

    /** 划扣信息表主键ID */
    private Long debitId;

    private String batNo;

    /** 交易流水号 */
    private String serialNo;

    private BigDecimal payAmount;

    private BigDecimal actualAmount;

    /** 请求时间 */
    private Date reqTime;

    /** 响应时间 */
    private Date resTime;

    /** 交易状态 */
    private String state;

    /** 返回码 */
    private String rtnCode;

    /** 返回信息 */
    private String rtnInfo;

    /** 备注 */
    private String memo;
    
    /** 合同编号 */
    private String pactNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getDebitId() {
        return debitId;
    }

    public void setDebitId(Long debitId) {
        this.debitId = debitId;
    }

    public String getBatNo() {
        return batNo;
    }

    public void setBatNo(String batNo) {
        this.batNo = batNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public Date getResTime() {
        return resTime;
    }

    public void setResTime(Date resTime) {
        this.resTime = resTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnInfo() {
        return rtnInfo;
    }

    public void setRtnInfo(String rtnInfo) {
        this.rtnInfo = rtnInfo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPactNo() {
        return pactNo;
    }

    public void setPactNo(String pactNo) {
        this.pactNo = pactNo;
    }
}
