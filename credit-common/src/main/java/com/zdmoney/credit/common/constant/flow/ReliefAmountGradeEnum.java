package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/9/27.
 */
public enum ReliefAmountGradeEnum {
    A("0＜如申请减免金额≤最大申请金额*100%","A"),
    B("0＜申请减免金额≤最大申请金额*80%","B"),
    C("最大申请金额*80%＜申请减免金额≤最大申请金额","C"),
    D("第一档","D"),
    E("第二档","E"),
    F("第三档","F");

    private String msg;

    private String code;

    ReliefAmountGradeEnum(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
