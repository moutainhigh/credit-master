package com.zdmoney.credit.common.constant;

/**
 * Created by ym10094 on 2017/5/15.
 */
public enum SpecialRepaymentApplyStatus {
    申请("1","申请"),
    通过("2","通过"),
    拒绝("3","拒绝"),
    取消("4","取消"),
    生效("5","生效"),
    失效("6","失效"),
    完成("7","完成");

    private String code;
    private String value;

    SpecialRepaymentApplyStatus(String code, String value) {
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
