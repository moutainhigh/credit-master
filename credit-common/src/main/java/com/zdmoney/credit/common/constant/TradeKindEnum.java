package com.zdmoney.credit.common.constant;

public enum TradeKindEnum {
	正常交易("正常交易"),
	冲正交易("正常交易");
	
	
	/** value*/
	private String value;
	
	private TradeKindEnum( String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
