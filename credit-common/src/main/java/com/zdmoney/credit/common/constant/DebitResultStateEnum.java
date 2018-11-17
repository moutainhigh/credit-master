package com.zdmoney.credit.common.constant;

/**
 * 划扣结果状态
 */
public enum DebitResultStateEnum {

    未划扣("1", "未划扣"), 
    划扣中("2", "划扣中"), 
    划扣失败("3", "划扣失败"), 
    划扣成功("4", "划扣成功");

    private String code;

    private String value;

    private DebitResultStateEnum(String code, String value) {
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
