package com.zdmoney.credit.common.constant;

/**
 * 当期是否结清状态
 */
public enum PayOffTypeEnum {

    未结清("1", "未结清"), 
    已结清("2", "已结清");

    private String code;

    private String value;

    private PayOffTypeEnum(String code, String value) {
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
