package com.zdmoney.credit.common.constant;

/**
 * 分账结果状态
 */
public enum SplitResultStateEnum {

    未分账("1", "未分账"), 
    分账中("2", "分账中"), 
    分账失败("3", "分账失败"), 
    分账成功("4", "分账成功");

    private String code;

    private String value;

    private SplitResultStateEnum(String code, String value) {
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
