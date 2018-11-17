package com.zdmoney.credit.repay.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BasePrivateAccountInfo extends BaseDomain {

    private static final long serialVersionUID = -8843956133277253022L;

    /**
     * id
     */
    private Long id;

    /**
     * 债权编号
     */
    private Long loanId;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 对方账户
     */
    private String secondAccount;

    /**
     * 对方姓名
     */
    private String secondName;

    /**
     * 交易行名
     */
    private String tradeBank;

    /**
     * 交易方式
     */
    private String tradeType;

    /**
     * 交易渠道
     */
    private String tradeChannel;

    /**
     * 交易用途
     */
    private String tradePurpose;

    /**
     * 交易说明
     */
    private String tradeDesc;

    /**
     * 交易摘要
     */
    private String tradeRemark;

    /**
     * 上传人
     */
    private Long operatorId;

    /**
     * 认领人
     */
    private Long recOperatorId;

    /**
     * 认领时间
     */
    private Date recTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String memo;
    
    //----------------------- 
    // 以下是非数据库字段
    //-----------------------
    /**
     * 交易日期起
     */
    private Date tradeDateStart;
    
    /**
     * 交易日期止
     */
    private Date tradeDateEnd;
    
    /**
     * 认领日期(起)
     */
    private Date recTimeStart;
    
    /**
     * 认领日期(止)
     */
    private Date recTimeEnd;
    
    /**
     * 认领人工号
     */
    private String recUsercode;

    /**
     * 内部流水号
     */
    private String repayNo;
    /**
     *  解锁状态 0-冻结 1-解锁
     */
    private String lockStatus;

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

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getSecondAccount() {
        return secondAccount;
    }

    public void setSecondAccount(String secondAccount) {
        this.secondAccount = secondAccount;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getTradeBank() {
        return tradeBank;
    }

    public void setTradeBank(String tradeBank) {
        this.tradeBank = tradeBank;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(String tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public String getTradePurpose() {
        return tradePurpose;
    }

    public void setTradePurpose(String tradePurpose) {
        this.tradePurpose = tradePurpose;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getRecOperatorId() {
        return recOperatorId;
    }

    public void setRecOperatorId(Long recOperatorId) {
        this.recOperatorId = recOperatorId;
    }

    public Date getRecTime() {
        return recTime;
    }

    public void setRecTime(Date recTime) {
        this.recTime = recTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getTradeDateStart() {
        return tradeDateStart;
    }

    public void setTradeDateStart(Date tradeDateStart) {
        this.tradeDateStart = tradeDateStart;
    }

    public Date getTradeDateEnd() {
        return tradeDateEnd;
    }

    public void setTradeDateEnd(Date tradeDateEnd) {
        this.tradeDateEnd = tradeDateEnd;
    }

    public Date getRecTimeStart() {
        return recTimeStart;
    }

    public void setRecTimeStart(Date recTimeStart) {
        this.recTimeStart = recTimeStart;
    }

    public Date getRecTimeEnd() {
        return recTimeEnd;
    }

    public void setRecTimeEnd(Date recTimeEnd) {
        this.recTimeEnd = recTimeEnd;
    }

    public String getRecUsercode() {
        return recUsercode;
    }

    public void setRecUsercode(String recUsercode) {
        this.recUsercode = recUsercode;
    }

	public String getRepayNo() {
		return repayNo;
	}

	public void setRepayNo(String repayNo) {
		this.repayNo = repayNo;
	}

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }
}