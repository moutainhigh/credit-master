package com.zdmoney.credit.ljs.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 虚拟帐户明细实体类
 * @author YM10104
 *
 */
public class PublicAccountDetail extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long accountId;
	private Date tradeDate;
	private String tradeTime;
	private String contractNum;
	private BigDecimal pay;
	private BigDecimal inCome;
	private BigDecimal initialBalance;
	private BigDecimal closingBalance;
	private String memo;
	private String tradeType;
	private String state;
	private String batchNo;
	private Date createTime;
	private Date updateTime;
	private String errorMsg;
	private BigDecimal reqMoney;
	
	public BigDecimal getReqMoney() {
		return reqMoney;
	}
	public void setReqMoney(BigDecimal reqMoney) {
		this.reqMoney = reqMoney;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public BigDecimal getPay() {
		return pay;
	}
	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}
	public BigDecimal getInCome() {
		return inCome;
	}
	public void setInCome(BigDecimal inCome) {
		this.inCome = inCome;
	}
	public BigDecimal getInitialBalance() {
		return initialBalance;
	}
	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
