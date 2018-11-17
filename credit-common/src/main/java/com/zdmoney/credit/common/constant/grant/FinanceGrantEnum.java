package com.zdmoney.credit.common.constant.grant;

/**
 * Created by ym10094 on 2016/11/10. 财务线上放款 --外贸2
 */
public enum FinanceGrantEnum {

    /** 财务放款状态 **/
    未放款("00", "未放款"), 
    申请中("01", "申请中"), 
    放款成功("02", "放款成功"), 
    放款失败("03", "放款失败");
    private String code;

    private String value;

    private FinanceGrantEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
