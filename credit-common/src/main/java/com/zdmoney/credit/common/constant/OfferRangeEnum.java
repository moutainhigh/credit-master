package com.zdmoney.credit.common.constant;

public enum OfferRangeEnum {
	M1("M1"),
	M7以下("M7-"),
	M7以上("M7+");
	/** value*/
	private String value;
	
	private OfferRangeEnum( String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
