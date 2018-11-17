package com.zdmoney.credit.common.constant;

/**
 * 对公账户认领系统类型枚举类
 * @author 00236640
 */
public enum PublicAccountSystemTypeEnum {

    /** 信贷系统 **/
    信贷("credit"),
    /** 车企贷系统 **/
    车企贷("car"),
    /** 证方系统 **/
    证方("zf");

    private String value;

    private PublicAccountSystemTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
