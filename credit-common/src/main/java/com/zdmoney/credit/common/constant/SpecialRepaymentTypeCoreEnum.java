package com.zdmoney.credit.common.constant;

public enum SpecialRepaymentTypeCoreEnum {
	onetime("onetime"), 
	reduction("reduction"), 
	advanceDeduct("reduction");
	
	/** value*/
	private String value;
	
	private SpecialRepaymentTypeCoreEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
