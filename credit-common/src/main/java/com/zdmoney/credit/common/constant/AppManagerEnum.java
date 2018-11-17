package com.zdmoney.credit.common.constant;

public enum AppManagerEnum {
	开启旧("0","开启旧"),
	未开启("1","关闭"),
	开启新("2","开启新");
	
	
	
	/** value*/
	private String value;
	
	/** code*/
	private String code;
	
	private AppManagerEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
