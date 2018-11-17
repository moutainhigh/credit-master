package com.zdmoney.credit.common.constant;

/**
 * 是否成功标志枚举
 * @author 00235304
 *
 */
public enum IsSucCoreEnum {

	succ("succ"),
	fail("fail");
	
	/** value*/
	private String value;
	
	private IsSucCoreEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
