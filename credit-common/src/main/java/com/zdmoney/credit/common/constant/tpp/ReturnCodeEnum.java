package com.zdmoney.credit.common.constant.tpp;


/**
 * TPP 划扣状态
 * 
 * @author Ivan
 *
 */
public enum ReturnCodeEnum {
	交易成功("000000", "交易成功"), 交易部分成功("444444", "交易部分成功"), 交易失败("111111", "交易失败");

	private String code;
	private String desc;

	private ReturnCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static ReturnCodeEnum get(String code) {
		ReturnCodeEnum[] returnCodeEnums;
		int j = (returnCodeEnums = values()).length;
		for (int i = 0; i < j; i++) {
			ReturnCodeEnum returnCodeEnum = returnCodeEnums[i];
			if (returnCodeEnum.getCode().equals(code)) {
				return returnCodeEnum;
			}
		}
		throw new IllegalArgumentException("ReturnCodeEnum is not exist : " + code);
	}

}
