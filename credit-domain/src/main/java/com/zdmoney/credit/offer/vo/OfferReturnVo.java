package com.zdmoney.credit.offer.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代扣回盘Vo
 * 
 * @author Ivan
 *
 */
public class OfferReturnVo {
	/** 债权编号 **/
	private Long loanId;
	/** 报盘日期 **/
	private Date reqTime;
	/** 借款人姓名 **/
	private String name;
	/** 借款人身份证号 **/
	private String idNum;
	/** 银行 **/
	private String bankName;
	/** 银行账号 **/
	private String bankAcct;
	/** 报盘金额 **/
	private BigDecimal payAmount;
	/** 回盘金额 **/
	private BigDecimal actualAmount;
	/** 回盘日期 **/
	private Date rspReceiveTime;
	/** 备注 **/
	private String memo;
	/** 划扣状态 **/
	private String trxState;
	/** TPP划扣状态返回码 **/
	private String rtnCode;
	/** 划扣方式 **/
	private String type;
	/** TPP划扣通道 **/
	private String paySysNo;
	/** 合同来源 **/
	private String fundsSources;
	/** 合同编号 **/
	private String contractNum;
	/** 债权去向 **/
	private String loanBelong;
	/** 营业部备注**/ 
	private String orgMemo;
	/** 回盘商户**/ 
	private String merId;
	/** 回购日期 **/
	private String buybackTime;
	/**转让批次**/
	private String transferBatch;
	
	public String getTransferBatch() {
		return transferBatch;
	}

	public void setTransferBatch(String transferBatch) {
		this.transferBatch = transferBatch;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrgMemo() {
		return orgMemo;
	}

	public void setOrgMemo(String orgMemo) {
		this.orgMemo = orgMemo;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Date getReqTime() {
		return reqTime;
	}

	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
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

	public Date getRspReceiveTime() {
		return rspReceiveTime;
	}

	public void setRspReceiveTime(Date rspReceiveTime) {
		this.rspReceiveTime = rspReceiveTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTrxState() {
		return trxState;
	}

	public void setTrxState(String trxState) {
		this.trxState = trxState;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}

	public String getLoanBelong() {
		return loanBelong;
	}

	public void setLoanBelong(String loanBelong) {
		this.loanBelong = loanBelong;
	}

	public String getBuybackTime() {
		return buybackTime;
	}

	public void setBuybackTime(String buybackTime) {
		this.buybackTime = buybackTime;
	}
}
