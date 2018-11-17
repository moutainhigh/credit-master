package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/9/14.
 */
public enum NodeTypeEnum {
    审批节点("01","审批节点"),
    申请节点("02","申请节点");

    private String code;
    private String value;

    NodeTypeEnum(String code, String value) {
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
