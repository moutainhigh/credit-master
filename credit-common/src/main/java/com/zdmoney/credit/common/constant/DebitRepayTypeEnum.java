package com.zdmoney.credit.common.constant;


public enum DebitRepayTypeEnum {

	委托还款("01","委托还款"),
	机构还款("02","机构还款"),
	逾期代偿("03","逾期代偿"),
	一次性回购("04","一次性回购"),
	提前结清("05","提前结清"),
	逾期还回("06","逾期还回"),
	回购结清("07","回购结清");
	
	private String code;

    private String value;

    private DebitRepayTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

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
