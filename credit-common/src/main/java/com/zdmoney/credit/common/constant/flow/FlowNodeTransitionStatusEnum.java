package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/8.
 */
public enum FlowNodeTransitionStatusEnum {
    审批中("审批中","0"),
    审批通过("通过","1"),
    审批拒绝("拒绝","2"),
    发起申请("申请","3"),
    取消申请("取消","4");

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

    FlowNodeTransitionStatusEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
