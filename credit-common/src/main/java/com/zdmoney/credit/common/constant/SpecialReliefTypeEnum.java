package com.zdmoney.credit.common.constant;

/**
 * Created by ym10094 on 2017/9/27.
 */
public enum SpecialReliefTypeEnum {
    特殊减免("是","1"),
    非特殊减免("否","0");
    private String value;
    private String code;

    SpecialReliefTypeEnum(String value, String code) {
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
