package com.zdmoney.credit.common.constant;

public enum LoanTypeReportEnum {
	随薪贷("随薪贷"),
	随意贷("随意贷"),
	随意贷A("随意贷A"),
	随意贷B("随意贷B"),
	随意贷C("随意贷C"),
	随房贷("随房贷"),
	助学贷("助学贷"),
	车贷("车贷"),
	薪生贷("薪生贷"),
	随车贷("随车贷"),
	随房贷A("随房贷A"),
	随房贷B("随房贷B"),
	公积金贷("公积金贷"),
	保单贷("保单贷"),
	网购达人贷A("网购达人贷A"),
	淘宝商户贷B("淘宝商户贷B"),
	学历贷("学历贷"),
	卡友贷("卡友贷");
	LoanTypeReportEnum(String value){
		this.value=value;
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
