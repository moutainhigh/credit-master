package com.zdmoney.credit.common.constant;

public enum SpecialRepaymentStateCoreEnum {

	application("application"),
	cancel("cancel"),
	over("over");
	
	/** value*/
	private String value;
	
	private SpecialRepaymentStateCoreEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
