package com.zdmoney.credit.common.constant.wm;

/**
 * Created by YM10098 on 2017/8/9.
 */
public enum WMDebitDeductStateEnum {
    未发送("未发送"),
    已发送("已发送"),
    扣款成功("扣款成功"),
    扣款失败("扣款失败");

    private String value;

    private WMDebitDeductStateEnum(String value){
        this.value = value;
    };

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
