package com.zdmoney.credit.common.constant;

/**
 *  Created by ym10094 on 2016/11/17.
 */
public enum OneTimeSettlementEnum {
    未申请审核("00","未申请审核"),
    审核中("01","审核中"),
    审核通过("02","审核通过"),
    审核失败("03","审核失败"),
    关闭申请("04","关闭申请");

    private String code;
    private String value;

    OneTimeSettlementEnum(String code, String value) {
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
