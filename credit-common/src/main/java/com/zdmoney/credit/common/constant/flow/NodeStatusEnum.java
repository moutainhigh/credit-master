package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/9/14.
 */
public enum NodeStatusEnum {
    有效("有效","0"),
    无效("无效","1");

    private String value;

    private String code;

    NodeStatusEnum(String value, String code) {
        this.value = value;
        this.code = code;
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
