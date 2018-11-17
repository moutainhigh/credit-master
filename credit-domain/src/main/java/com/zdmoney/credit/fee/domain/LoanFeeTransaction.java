package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款收费回盘表
 * 
 * @author Ivan
 *
 */
public class LoanFeeTransaction extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8063428296853959117L;
	/** 主键 **/
	private Long id;
	/** 债权编号 **/
	private Long loanId;
	/** 收费主表编号 **/
	private Long feeId;
	/** 报盘编号 **/
	private Long offerId;
	/** 流水号 **/
	private String serialNo;
	/** 应划扣金额 **/
	private BigDecimal amount;
	/** 实际划扣金额 **/
	private BigDecimal factAmount;
	/** TPP回盘日志编号 **/
	private Long logId;
	/** 交易请求时间 **/
	private Date requestTime;
	/** 交易响应时间 **/
	private Date responseTime;
	/** TPP返回状态码（000000 已回盘全额 444444 已回盘非全额 111111 未扣到款项） **/
	private String rtnCode;
	/** TP返回信息（主要记录失败原因） **/
	private String rtnInfo;
	/** 交易状态 **/
	private String trxState;
	/** 预留备注信息 **/
	private String memo;
	/** TPP返回通道code **/
	private String paySysNoReal;
	/** TPP返回划扣商户号 **/
	private String merId;
	
	

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getPaySysNoReal() {
		return paySysNoReal;
	}

	public void setPaySysNoReal(String paySysNoReal) {
		this.paySysNoReal = paySysNoReal;
	}

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

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo == null ? null : serialNo.trim();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode == null ? null : rtnCode.trim();
	}

	public String getRtnInfo() {
		return rtnInfo;
	}

	public void setRtnInfo(String rtnInfo) {
		this.rtnInfo = rtnInfo == null ? null : rtnInfo.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public String getTrxState() {
		return trxState;
	}

	public void setTrxState(String trxState) {
		this.trxState = trxState;
	}
	
}