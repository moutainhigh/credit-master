package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/4.
 */
public enum OverDueGradeEnum {
    A("逾期等级 1<=逾期天数<=15","A"),
    B("逾期等级 15<逾期天数<=31","B"),
    D("规则内","D"),
    E("规则外","E");

    private String msg;

    private String code;

    OverDueGradeEnum(String msg, String code) {
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
