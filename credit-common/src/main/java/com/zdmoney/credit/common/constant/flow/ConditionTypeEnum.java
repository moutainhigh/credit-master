package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/4.
 */
public enum ConditionTypeEnum {
    逾期等级("逾期等级","01"),
    营业部门("营业部门","02"),
    减免类型("减免类型","03"),
    流程首节点("流程首节点","04"),
    减免金额等级("减免金额等级","05")
    ;

    private String value;

    private String code;

    ConditionTypeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
