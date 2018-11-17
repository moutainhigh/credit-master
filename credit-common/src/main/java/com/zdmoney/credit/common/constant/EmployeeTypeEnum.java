package com.zdmoney.credit.common.constant;

public enum EmployeeTypeEnum {
	管理人员("管理人员"),
	业务员("业务员"),
	客服("客服"),
	行政助理("行政助理"),
	审核员("审核员"),
	审批员("审批员"),
	系统管理员("系统管理员"),
	催收员("催收员"),
	催收管理("催收管理");
	
	private String value;	    
	private EmployeeTypeEnum( String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	 }
	public void setValue(String value) {
		this.value = value;
	 }
}
