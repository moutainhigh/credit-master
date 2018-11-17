package com.zdmoney.credit.common.constant;

/**
 * 币种
 * @author 00232949
 *
 */
public enum CurrencyTypeEnum {
	
	CNY("CNY");
	
	/** value*/
	private String value;
	
	private CurrencyTypeEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
