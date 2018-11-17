package com.zdmoney.credit.common.constant;

/**
 * 报盘类型
 * @author 00232949
 *
 */
public enum OfferTypeEnum {
	自动划扣("自动划扣"),
	实时划扣("实时划扣");
	
	/** value*/
	private String value;
	
	private OfferTypeEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
