package com.zdmoney.credit.common.constant;

/**
 * 分账通知状态
 */
public enum SplitNotifyStateEnum {

    待通知("1", "待通知"), 
    已通知("2", "已通知"), 
    通知失败("3", "通知失败"), 
    通知成功("4", "通知成功");

    private String code;

    private String value;

    private SplitNotifyStateEnum(String code, String value) {
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
