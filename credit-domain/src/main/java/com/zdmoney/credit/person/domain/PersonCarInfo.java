package com.zdmoney.credit.person.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款人车辆信息实体
 * @author Ivan
 *
 */
public class PersonCarInfo extends BaseDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7893401596166170866L;
	
	/** 主键id **/
    private Long id;
    /** 借款人person表id **/
    private Long personId;
    /** 购买时间 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date buyTime;
    /** 车辆类型 **/
    private String carType;
    /** 是否有贷款 **/
    private String hasLoan;
    /** 借款期限 **/
    private Long loanPeriod;
    /** 月供 **/
    private Double monthlyPayment;
    /** 购买价格 **/
    private Double price;
    /**车贷发放年月**/
    private Date carLoanDate;
    
	public Date getCarLoanDate() {
		return carLoanDate;
	}
	public void setCarLoanDate(Date carLoanDate) {
		this.carLoanDate = carLoanDate;
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
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getHasLoan() {
		return hasLoan;
	}
	public void setHasLoan(String hasLoan) {
		this.hasLoan = hasLoan;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
    
}