package com.zdmoney.credit.common.constant;

public enum SysOperationTypeEnum {
	RESETPWD(1);
	
	
	/** value*/
	private int value;
	
	private SysOperationTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
