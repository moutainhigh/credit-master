package com.zdmoney.credit.common.constant;

/**
 * 信托融资放款状态
 * @author 00232949
 *
 */
public enum TrustGrantStateEnum {
	融资成功("融资成功"), 
	融资失败("融资失败"), 
	融资处理中("融资处理中");
	
	
	/** value*/
	private String value;
	
	private TrustGrantStateEnum(String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
