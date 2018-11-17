package com.zdmoney.credit.common.constant;

/**
 * @author 10098  2017年3月30日 下午4:13:35
 */
public enum SubjTypeWm3Enum {

	应收本金("1001", "应收本金"),
	应收利息("1002", "应收利息"),
	应收罚息("1003", "应收罚息"),
	实收本金("2001", "实收本金"),
	实收利息("2002", "实收利息"),
	实收罚息("2003", "实收罚息"),
	减免本金("3001", "减免本金"),
	减免利息("3002", "减免利息"),
	减免罚息("3003", "减免罚息"),
	代收违约金("4104", "代收违约金"),
	自收违约金("4204", "自收违约金");
	
	private SubjTypeWm3Enum(String code, String value){
		this.code = code;
		this.value= value;
	}
	private String code;
	private String value;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
