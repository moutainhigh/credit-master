package com.zdmoney.credit.common.constant.wm;

/**
 * 扣款类型（线下）
 * @author 00236640
 *
 */
public enum WMUnderlineRepayTypeEnum {

    线下正常扣款("01", "线下正常扣款"), 
    线下提前清贷("02", "线下提前清贷"), 
    线下溢缴款充值("03", "线下溢缴款充值");

    /** value */
    private String value;

    /** code */
    private String code;

    private WMUnderlineRepayTypeEnum(String code, String value) {
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