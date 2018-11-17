package com.zdmoney.credit.offer.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款录入 Vo
 * @author Ivan
 *
 */
public class RepaymentInputVo {
	/** 借款编号 **/
	Long loanId;
	/** 还款金额 **/
	BigDecimal amount;
	/** 备注 **/
	String memo;
	/** 机构Code **/
	String organ;
	/** 操作人工号 **/
	String teller;
	/** 交易时间 **/
	Date tradeDate;
	/** 类型 **/
	String tradeType;
	/** 内部流水号 **/
	String repayNo;

	Date tradeTime;
	boolean isTppSouces = false;
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	public String getTeller() {
		return teller;
	}
	public void setTeller(String teller) {
		this.teller = teller;
	}
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
	public String getRepayNo() {
		return repayNo;
	}
	public void setRepayNo(String repayNo) {
		this.repayNo = repayNo;
	}

	public boolean isTppSouces() {
		return isTppSouces;
	}

	public void setIsTppSouces(boolean isTppSouces) {
		this.isTppSouces = isTppSouces;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
}
