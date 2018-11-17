package com.zdmoney.credit.system.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 法定节假日表
 * @author 00232949
 *
 */
public class SysSpecialDay extends BaseDomain{
    private Long id;

    private Date specailDay;

    private String workday;

    public SysSpecialDay(Date specailDay,boolean  workday){
        this.specailDay = specailDay;
        this.workday = workday==true?"t":"f";
    }
    
    public SysSpecialDay() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSpecailDay() {
        return specailDay;
    }

    public void setSpecailDay(Date specailDay) {
        this.specailDay = specailDay;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday == null ? null : workday.trim();
    }
}