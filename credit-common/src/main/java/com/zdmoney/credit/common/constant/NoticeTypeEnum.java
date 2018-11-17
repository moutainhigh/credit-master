package com.zdmoney.credit.common.constant;

/**
 * Created by YM10098 on 2017/8/25.
 */
public enum NoticeTypeEnum {
    展业("展业"),
    贷后("贷后");

    private String value;

    NoticeTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
