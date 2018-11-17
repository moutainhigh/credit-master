package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.util.Date;

public class LoanBsbMapping  extends BaseDomain{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	/** 业务流水号*/
	private String busNumber;
	
	/** 借据号*/
	private String orderNo;
	
	/** 借款ID*/
	private Long loanId;

	/** 起息日 **/
	private Date valueDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
}
