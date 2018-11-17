package com.zdmoney.credit.common.constant;

/**
 * 扣款帐户渠道
 */
public enum PayPartyEnum {

    借款人("1", "借款人"), 
    服务公司("2", "服务公司"), 
    准备金("3", "准备金"), 
    保证金("4", "保证金");

    private String code;

    private String value;

    private PayPartyEnum(String code, String value) {
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
