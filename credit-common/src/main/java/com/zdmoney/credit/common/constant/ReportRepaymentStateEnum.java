package com.zdmoney.credit.common.constant;

public enum ReportRepaymentStateEnum {
	提前还清("提前还清"),
	正常结清("正常结清"),
	逾期结清("逾期结清"),
	预结清("预结清");
	ReportRepaymentStateEnum(String value){
		this.value = value;
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
