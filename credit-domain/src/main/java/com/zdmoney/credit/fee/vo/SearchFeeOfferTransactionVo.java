package com.zdmoney.credit.fee.vo;

import com.zdmoney.credit.common.util.Dates;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务费回盘Vo
 * 
 * @author Ivan
 */
public class SearchFeeOfferTransactionVo {
	/** 报盘表ID **/
	private Long id;
	/** 客户姓名 **/
	private String name;
	/** 身份证号码 **/
	private String idNum;
	/** 合同编号 **/
	private String contractNum;
	/** 合同来源 **/
	private String fundsSources;
	/** 银行名称 **/
	private String bankName;
	/** 银行卡号 **/
	private String bankAcct;
	/** 报盘日期 **/
	private Date offerDate;
	/** 报盘日期字符串 **/
	private String requestTimeStr;
	/** 报盘金额 **/
	private BigDecimal amount;
	/** 回盘金额 **/
	private BigDecimal factAmount;
	/** 回盘日期 **/
	private Date responseTime;
	/** 回盘日期字符串 **/
	private String responseTimeStr;
	/** 类型 **/
	private String type;
	/** 划扣状态 **/
	private String trxState;
	/** 划扣通道 **/
	private String paySysNo;
	/** 划扣状态(000000 交易成功  444444交易部分成功 000000 交易失败) **/
	private String rtnCode;
    /**营业部备注**/
	private String orgMemo;
	/** TPP划扣通道 **/
	private String paySysNoReal;
	/** 划扣商户号 **/
	private String merId;
	/**失败原因**/
	private String failReason;
	
	
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

	public String getOrgMemo() {
		return orgMemo;
	}

	public void setOrgMemo(String orgMemo) {
		this.orgMemo = orgMemo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
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

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
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

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTrxState() {
		return trxState;
	}

	public void setTrxState(String trxState) {
		this.trxState = trxState;
	}

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}

	public String getRequestTimeStr() {
		return requestTimeStr;
	}

	public void setRequestTimeStr(String requestTimeStr) {
		this.requestTimeStr = requestTimeStr;
	}

	public String getResponseTimeStr() {
		return responseTimeStr;
	}

	public void setResponseTimeStr(String responseTimeStr) {
		this.responseTimeStr = responseTimeStr;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
}