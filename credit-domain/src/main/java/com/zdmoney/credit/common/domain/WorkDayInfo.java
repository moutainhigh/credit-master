package com.zdmoney.credit.common.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class WorkDayInfo extends BaseDomain {
	private static final long serialVersionUID = 6711288279445906083L;

	private Long id;
	//当前日期
	private Date currDate;
	//是否工作日 1-是 0-不是
	private String isWorkDay;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCurrDate() {
		return currDate;
	}
	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}
	public String getIsWorkDay() {
		return isWorkDay;
	}
	public void setIsWorkDay(String isWorkDay) {
		this.isWorkDay = isWorkDay;
	}
	
	
}
