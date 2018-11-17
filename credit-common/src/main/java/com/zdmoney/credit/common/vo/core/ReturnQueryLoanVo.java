package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

public class ReturnQueryLoanVo {
	String borrower;
    String sex;
    String idnum;
    String loanType;//借款类型
    String organizationName;//所属机构(渠道)
    String planName;//借款方案(渠道)
    BigDecimal requestMoney;//申请金额
    Integer requestTime;//申请期限
    String requestDate;//申请日期
    BigDecimal money;//审批金额
    Integer time;//借款期限
    BigDecimal maxMonthlyPayment;//可接受的最高月还款额
    String loanFlowState;//状态
    BigDecimal restoreEM;//月还款能力
    String purpose;//用途
    String salesDepartment;//管理营业部
    String giveBackBank;//还款银行
    String grantBank;//放款银行
    String salesman;//客户经理
    String crm;//客服
    String fundsSources;//合同来源
    String remark;//备注
    Integer residualTime;//剩余未结清期数 
    BigDecimal grantMoney; //放款金额
    
	public BigDecimal getGrantMoney() {
		return grantMoney;
	}
	public void setGrantMoney(BigDecimal grantMoney) {
		this.grantMoney = grantMoney;
	}
	public Integer getResidualTime() {
		return residualTime;
	}
	public void setResidualTime(Integer residualTime) {
		this.residualTime = residualTime;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public BigDecimal getRequestMoney() {
		return requestMoney;
	}
	public void setRequestMoney(BigDecimal requestMoney) {
		this.requestMoney = requestMoney;
	}
	public Integer getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Integer requestTime) {
		this.requestTime = requestTime;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public BigDecimal getMaxMonthlyPayment() {
		return maxMonthlyPayment;
	}
	public void setMaxMonthlyPayment(BigDecimal maxMonthlyPayment) {
		this.maxMonthlyPayment = maxMonthlyPayment;
	}
	public String getLoanFlowState() {
		return loanFlowState;
	}
	public void setLoanFlowState(String loanFlowState) {
		this.loanFlowState = loanFlowState;
	}
	public BigDecimal getRestoreEM() {
		return restoreEM;
	}
	public void setRestoreEM(BigDecimal restoreEM) {
		this.restoreEM = restoreEM;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSalesDepartment() {
		return salesDepartment;
	}
	public void setSalesDepartment(String salesDepartment) {
		this.salesDepartment = salesDepartment;
	}
	public String getGiveBackBank() {
		return giveBackBank;
	}
	public void setGiveBackBank(String giveBackBank) {
		this.giveBackBank = giveBackBank;
	}
	public String getGrantBank() {
		return grantBank;
	}
	public void setGrantBank(String grantBank) {
		this.grantBank = grantBank;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getCrm() {
		return crm;
	}
	public void setCrm(String crm) {
		this.crm = crm;
	}
	public String getFundsSources() {
		return fundsSources;
	}
	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
