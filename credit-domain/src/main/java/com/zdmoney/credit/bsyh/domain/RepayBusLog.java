package com.zdmoney.credit.bsyh.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class RepayBusLog extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long loanId;
	private String currentTerm;
	private String applyState;
	private String deductState;
	private String reternMsg;
	private String respCd;
	private String repayBusNumber;
	private Date createTime;
	private Date updateTime;
	/**
	 * 1申请提前扣款 2申请提前一次性结清
	 */
	private Long applyType;
	
	
	public Long getApplyType() {
		return applyType;
	}
	public void setApplyType(Long applyType) {
		this.applyType = applyType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRepayBusNumber() {
		return repayBusNumber;
	}
	public void setRepayBusNumber(String repayBusNumber) {
		this.repayBusNumber = repayBusNumber;
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
	public String getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(String currentTerm) {
		this.currentTerm = currentTerm;
	}
	public String getApplyState() {
		return applyState;
	}
	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}
	public String getDeductState() {
		return deductState;
	}
	public void setDeductState(String deductState) {
		this.deductState = deductState;
	}
	public String getReternMsg() {
		return reternMsg;
	}
	public void setReternMsg(String reternMsg) {
		this.reternMsg = reternMsg;
	}
	public String getRespCd() {
		return respCd;
	}
	public void setRespCd(String respCd) {
		this.respCd = respCd;
	}	
}
