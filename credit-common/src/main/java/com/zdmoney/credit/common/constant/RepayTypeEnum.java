package com.zdmoney.credit.common.constant;

public enum RepayTypeEnum {

	onetime("onetime"), 
	normal("normal");

	/** value */
	private String value;

	private RepayTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
