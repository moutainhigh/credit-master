package com.zdmoney.credit.common.constant.flow;

/**
 * Created by ym10094 on 2017/8/4.
 */
public enum DepartmentTypeEnum {
    营业部("营业部"),
    催收管理室("催收管理室");

    private String value;

    DepartmentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

