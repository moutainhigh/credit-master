package com.zdmoney.credit.common.constant;

/**
 * 对公账户操作类型枚举类
 * @author 00236640
 */
public enum PublicAccountOperateTypeEnum {

    认领("1"),
    撤销认领("2"),
    渠道确认("3"),
    撤销渠道确认("4");
    
    private String value;

    private PublicAccountOperateTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
