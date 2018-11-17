package com.zdmoney.credit.common.constant;

/**
 * 垫付类型
 */
public enum CompensatoryTypeEnum {

	逾期代偿("01", "逾期代偿"), 
	一次性回购("02", "一次性回购"), 
    委托还款("03", "委托还款");

    private String code;

    private String value;

    private CompensatoryTypeEnum(String code, String value) {
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
