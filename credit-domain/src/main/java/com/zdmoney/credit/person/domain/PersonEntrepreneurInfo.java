package com.zdmoney.credit.person.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款人企业经营信息实体
 * @author Ivan
 *
 */
public class PersonEntrepreneurInfo extends BaseDomain {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -61759589089100935L;
	/** 主键id **/
	private Long id;
	/** persion表id **/
    private Long personId;
    /** 员工数量 **/
    private Long employeeAmount;
    /** 企业类型 **/
    private String enterpriseType;
    /** 月净利润 **/
    private Double monthlyNetProfit;
    /** 其他类型 **/
    private String otherType;
    /** 经营场地类型 **/
    private String premisesType;
    /** 利润率 **/
    private Double profitMargin;
    /** 持股比例 **/
    private Double shareholdingRatio;
    /** 成立时间 **/
    private String timeFounded;

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

	public Long getEmployeeAmount() {
		return employeeAmount;
	}

	public void setEmployeeAmount(Long employeeAmount) {
		this.employeeAmount = employeeAmount;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public Double getMonthlyNetProfit() {
		return monthlyNetProfit;
	}

	public void setMonthlyNetProfit(Double monthlyNetProfit) {
		this.monthlyNetProfit = monthlyNetProfit;
	}

	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public String getPremisesType() {
		return premisesType;
	}

	public void setPremisesType(String premisesType) {
		this.premisesType = premisesType;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Double getShareholdingRatio() {
		return shareholdingRatio;
	}

	public void setShareholdingRatio(Double shareholdingRatio) {
		this.shareholdingRatio = shareholdingRatio;
	}

	public String getTimeFounded() {
		return timeFounded;
	}

	public void setTimeFounded(String timeFounded) {
		this.timeFounded = timeFounded;
	}

}