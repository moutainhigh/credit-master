package com.zdmoney.credit.common.constant.wm;

/**
 * 扣款类型（线上）
 * @author 00236640
 *
 */
public enum WMOnlineRepayTypeEnum {

    正常扣款("01", "正常扣款"), 
    提前清贷("02", "提前清贷"), 
    溢缴款充值("03", "溢缴款充值");

    /** value */
    private String value;

    /** code */
    private String code;

    private WMOnlineRepayTypeEnum(String code, String value) {
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