package com.zdmoney.credit.person.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款人房产信息实体
 * @author Ivan
 *
 */
public class PersonHouseInfo extends BaseDomain {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8410491003879391766L;
	/** 主键id **/
	private Long id;
	/** persion表id **/
    private Long personId;
    /** 房产地址 **/
    private String address;
    /** 建筑面积 **/
    private Double buildingArea;
    /** 购买时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date buyTime;
    /** 贷款余额 **/
    private Double loanBalance;
    /** 借款期限 **/
    private Long loanPeriod;
    /** 月供 **/
    private Double monthlyPayment;
    /** 邮编 **/
    private String postcode;
    /** 购买价格 **/
    private Double price;
    /** 产权比例 **/
    private Double propertyProportion;
    /** 单元价格 **/
    private Double unitPrice;
    /** 是否有贷款 **/
    private String hasLoan;
    /**房贷发放年月**/
    private Date estateLoanDate;

	public Date getEstateLoanDate() {
		return estateLoanDate;
	}

	public void setEstateLoanDate(Date estateLoanDate) {
		this.estateLoanDate = estateLoanDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(Double buildingArea) {
		this.buildingArea = buildingArea;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public Double getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}

	public Long getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(Long loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public Double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(Double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPropertyProportion() {
		return propertyProportion;
	}

	public void setPropertyProportion(Double propertyProportion) {
		this.propertyProportion = propertyProportion;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getHasLoan() {
		return hasLoan;
	}

	public void setHasLoan(String hasLoan) {
		this.hasLoan = hasLoan;
	}
	
}