package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 代扣管理显示vo
 * @author 10098  2017年5月18日 下午3:17:33
 */
public class DebitQueueManagementVo extends BaseDomain{

	private static final long serialVersionUID = 8185652219806476287L;

	private Long debitId;
	
	private Long loanId;
	
	private String custName;
	
	private String idNum;
	
	private String contractNum;
	
	private String debitNotifyState;

    private String debitResultState;

    private String debitType;
    
    private String batchId;
    
    private String debitNo;
    
	private String deductStartDate;
	
	private String deductEndDate;
	
	private String deductTime;

	private BigDecimal amount; 
	
	private String payParty;

	private String repayType;

	private String memo;
	
	private Long repayTerm;
	
	private String tradeNo;
	/** 冻结金额（回盘金额） **/
	private BigDecimal frozenAmount;
	/** 划扣回盘时间  **/
	private Date returnTime;

	public Long getDebitId() {
		return debitId;
	}

	public void setDebitId(Long debitId) {
		this.debitId = debitId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
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

	public String getDebitNotifyState() {
		return debitNotifyState;
	}

	public void setDebitNotifyState(String debitNotifyState) {
		this.debitNotifyState = debitNotifyState;
	}

	public String getDebitResultState() {
		return debitResultState;
	}

	public void setDebitResultState(String debitResultState) {
		this.debitResultState = debitResultState;
	}

	public String getDebitType() {
		return debitType;
	}

	public void setDebitType(String debitType) {
		this.debitType = debitType;
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

	public String getDeductStartDate() {
		return deductStartDate;
	}

	public void setDeductStartDate(String deductStartDate) {
		this.deductStartDate = deductStartDate;
	}

	public String getDeductEndDate() {
		return deductEndDate;
	}

	public void setDeductEndDate(String deductEndDate) {
		this.deductEndDate = deductEndDate;
	}

	public String getDeductTime() {
		return deductTime;
	}

	public void setDeductTime(String deductTime) {
		this.deductTime = deductTime;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

    public String getPayParty() {
        return payParty;
    }

    public void setPayParty(String payParty) {
        this.payParty = payParty;
    }

	public Long getRepayTerm() {
		return repayTerm;
	}

	public void setRepayTerm(Long repayTerm) {
		this.repayTerm = repayTerm;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
}
