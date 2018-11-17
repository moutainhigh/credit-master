package com.zdmoney.credit.common.constant;

/**
 * 核心计算器版本
 * 
 * @author Ivan
 *
 */
public enum CalculatorVersionEnum {
	v1("v1"), v2("v2");

	private String value;

	private CalculatorVersionEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
