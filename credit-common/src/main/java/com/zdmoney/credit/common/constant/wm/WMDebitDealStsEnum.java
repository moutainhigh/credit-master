package com.zdmoney.credit.common.constant.wm;

/**
 * 扣款处理状态
 * @author 00236640
 *
 */
public enum WMDebitDealStsEnum {

    未处理("01", "未处理"), 
    处理中("02", "处理中"), 
    处理成功("03", "处理成功"),
    处理失败("04", "处理失败"), 
    服务异常("05", "服务异常");

    /** value */
    private String value;

    /** code */
    private String code;

    private WMDebitDealStsEnum(String code, String value) {
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