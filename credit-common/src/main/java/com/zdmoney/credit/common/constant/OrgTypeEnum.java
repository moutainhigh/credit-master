package com.zdmoney.credit.common.constant;

public enum OrgTypeEnum {
	学校("学校"),
	车行("车行");
	
	/** value*/
	private String value;
	
	private OrgTypeEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
