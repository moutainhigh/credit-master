package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/9.
 */
public enum NodeActionTypeEnum {
    自动("自动","0"),
    手动("手动","1");

    private String value;

    private String code;

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

    NodeActionTypeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
