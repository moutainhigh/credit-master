package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分账管理页面所需参数
 * @author YM10112
 *
 */
public class SplitQueueManangeVO {

	private Long splitId;
	private Long loanId; 			
	private String name;
	private String idNum;	
	private String contractNum;
	private String batchId;
	private String debitNo;	
	private Date createTime;
	private String splitNotifyState;
	private String splitResultState;	
	private String payOffType;
	private String repayType;
	private BigDecimal amount ;
	
	public Long getSplitId() {
		return splitId;
	}
	public void setSplitId(Long splitId) {
		this.splitId = splitId;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
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
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getDebitNo() {
		return debitNo;
	}
	public void setDebitNo(String debitNo) {
		this.debitNo = debitNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSplitNotifyState() {
		return splitNotifyState;
	}
	public void setSplitNotifyState(String splitNotifyState) {
		this.splitNotifyState = splitNotifyState;
	}
	public String getSplitResultState() {
		return splitResultState;
	}
	public void setSplitResultState(String splitResultState) {
		this.splitResultState = splitResultState;
	}
	public String getPayOffType() {
		return payOffType;
	}
	public void setPayOffType(String payOffType) {
		this.payOffType = payOffType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	} 	
	
}
