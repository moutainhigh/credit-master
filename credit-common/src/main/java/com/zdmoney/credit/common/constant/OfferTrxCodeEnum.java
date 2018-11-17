package com.zdmoney.credit.common.constant;

/**
 * 交易代码，用于区分交易类型<p>
 * 
 * 编码原则：1开头：正向交易， 2开头，反向交易，3开头，撤销类交易
 * @author 00232949
 *
 */
public enum OfferTrxCodeEnum {
	//
	自动划扣("1001"),
	手动划扣("1002"),
	撤销划扣("3001"),
	提现("2001");
	
	/** value*/
	private String value;
	
	private OfferTrxCodeEnum( String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
