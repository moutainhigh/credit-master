package com.zdmoney.credit.common.constant.wm;

/**
 * 线上标示
 * @author 00236640
 *
 */
public enum WMOnlineFlagEnum {

    线上扣款("01", "线上扣款"), 
    线下实收("02", "线下实收");

    /** value */
    private String value;

    /** code */
    private String code;

    private WMOnlineFlagEnum(String code, String value) {
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