package com.zdmoney.credit.common.constant;


public enum AccountStateEnum {
	正常("01", "正常"),	
	逾期("02", "逾期"),	
	结清("03", "结清"),	
	呆账("04", "呆账"),	
	转出("05", "转出"),	
	退货("10", "退货");

	private String code;
	
	private String value;
	
	private AccountStateEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	
	
}
