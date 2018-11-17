package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

/**
 * 回盘信息查询接口返回值类型
 * 
 * @author 00235304
 * 
 */
public class OfferVO {

	private Long loanId; 		// 债权ID
	private Long offerId; 		// 流水ID
	private String date; 		// 报盘日期，格式为yyyy-MM-dd，例如：2015-03-13。
	private String name; 		// 账户名
	private String bankName;	// 银行名称
	private String bankAcct;	// 银行帐号
	private String returnDate;	// 回盘日期，格式为yyyy-MM-dd，例如：2015-03-13。
	private String cause; 		// 备注
	private String state; 		// 状态
	private String type; 		// 划扣类型
	private String isSuc; 		// 扣款是否成功.
	private String number; 		// 序号（报盘序号）
	private BigDecimal amount = BigDecimal.ZERO;// 应收金额（应报金额）
	private BigDecimal offerAmount = BigDecimal.ZERO;// 报盘金额（实报金额）
	private String tppType; 	//划扣通道
	private String memo;		//备注
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAcct() {
		return bankAcct;
	}

	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsSuc() {
		return isSuc;
	}

	public void setIsSuc(String isSuc) {
		this.isSuc = isSuc;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(BigDecimal offerAmount) {
		this.offerAmount = offerAmount;
	}

	public String getTppType() {
		return tppType;
	}

	public void setTppType(String tppType) {
		this.tppType = tppType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
