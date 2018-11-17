package com.zdmoney.credit.common.constant;

/**
 * 报盘文件状态
 * @author 00232949
 *
 */
public enum OfferTransactionStateEnum {
	
	未发送("未发送"),
	已发送("已发送"),
	扣款成功("扣款成功"),
	扣款失败("扣款失败"),
	扣款不明("扣款不明");
	
	/** value*/
	private String value;
	
	private OfferTransactionStateEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
