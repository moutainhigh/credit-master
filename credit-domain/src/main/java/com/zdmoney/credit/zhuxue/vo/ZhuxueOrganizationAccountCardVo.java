package com.zdmoney.credit.zhuxue.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.common.annotation.ConvertionCLazzType;

public class ZhuxueOrganizationAccountCardVo {

	private String name;
	private Date tradeDate;
	private String tradeType;
	private String tradeCode;
	private BigDecimal tradeAmount;
	private String tradeCodeStr;//交易类型
	private BigDecimal income;//收入
	private BigDecimal payment;//支出
	
	private Long zhuxueOrganizationId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(value=java.util.Date.class)
	private Date startDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(value=java.util.Date.class)
	private Date endDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getTradeCodeStr() {
		return tradeCodeStr;
	}

	public void setTradeCodeStr(String tradeCodeStr) {
		this.tradeCodeStr = tradeCodeStr;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Long getZhuxueOrganizationId() {
		return zhuxueOrganizationId;
	}

	public void setZhuxueOrganizationId(Long zhuxueOrganizationId) {
		this.zhuxueOrganizationId = zhuxueOrganizationId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
