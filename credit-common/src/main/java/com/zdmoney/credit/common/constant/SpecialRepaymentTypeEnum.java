package com.zdmoney.credit.common.constant;

/**
 * 特殊还款表类别枚举
 * @author 00232949
 *
 */
public enum SpecialRepaymentTypeEnum {
	一次性还款("一次性还款"),
	减免("减免"),
	提前扣款("提前扣款"),
	结算单("结算单"),
	正常费用减免 ("正常费用减免");
	
	/** value*/
	private String value;
	
	private SpecialRepaymentTypeEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
