package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/8.
 */
public enum FlowInstanceStatusEnum {
    开启流程("开启流程","0"),
    结束流程("结束流程","1"),
    取消流程("取消流程","2");

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

    FlowInstanceStatusEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
