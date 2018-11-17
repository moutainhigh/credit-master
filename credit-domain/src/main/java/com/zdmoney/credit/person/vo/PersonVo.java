package com.zdmoney.credit.person.vo;

import java.util.Date;

public class PersonVo {
	
	private String name;	//客户姓名
	private String mobile;	//手机号码
	private String employeeName; // 员工名称
	private Date leaveOfficeDate;//离职时间
	private Long employeeId; //员工id
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Date getLeaveOfficeDate() {
		return leaveOfficeDate;
	}
	public void setLeaveOfficeDate(Date leaveOfficeDate) {
		this.leaveOfficeDate = leaveOfficeDate;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	
	
}
