package com.zdmoney.credit.ljs.vo;

import java.math.BigDecimal;
/**
 * 导出账户流水明细vo
 * @author YM10104
 *
 */
public class ImportAccountDetailVo{
	
	private String contractNum;
	private String tradeDate;
	private String tradeTime;
	private BigDecimal pay;
	private BigDecimal inCome;
	private BigDecimal closingBalance;
	private String memo;
	private String tradeType;
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
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
}
