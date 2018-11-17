package com.zdmoney.credit.repay.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;


public class VCustomerAccountManagerList {
	@Length(min=0,max=80,message="客户名称不能超过80个字符")
	private String customerName;
	
	@Length(min=0,max=80,message="客户身份证件号不能超过80个字符")
	private String customerIdNum;
	
	//合同编号
	private String contractNum;
	@NotNull(message="页不能为空")
	private Integer page;
	
	@NotNull(message="页大小不能为空")
	private Integer rows;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerIdNum() {
		return customerIdNum;
	}

	public void setCustomerIdNum(String customerIdNum) {
		this.customerIdNum = customerIdNum;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	
}
