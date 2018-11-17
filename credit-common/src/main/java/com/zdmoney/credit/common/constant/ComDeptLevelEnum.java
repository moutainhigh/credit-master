package com.zdmoney.credit.common.constant;


/**
 * @author 00236633
 * 部门级别
 *
 */
public enum ComDeptLevelEnum {
	A("A"),
	B("B"),
	C("C"),
	D("D"),
	O("O");
	private String value;	    
	private ComDeptLevelEnum( String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	 }
	public void setValue(String value) {
		this.value = value;
	 }
	
}