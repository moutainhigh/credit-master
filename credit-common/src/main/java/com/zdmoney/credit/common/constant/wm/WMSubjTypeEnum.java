package com.zdmoney.credit.common.constant.wm;

/**
 * 科目类型
 * @author 00236640
 *
 */
public enum WMSubjTypeEnum {

    应收本金("1001", "应收本金"), 
    应收利息("1002", "应收利息"), 
    应收罚息("1003", "应收罚息"),
    
    实收本金("2001", "实收本金"), 
    实收利息("2002", "实收利息"), 
    实收罚息("2003", "实收罚息"),
    
    减免本金("3001", "减免本金"), 
    减免利息("3002", "减免利息"), 
    减免罚息("3003", "减免罚息"),
    
    代收趸交费("4101", "代收趸交费"), 
    代收服务费("4102", "代收服务费（期缴）"), 
    代收保费("4103", "代收保费（期缴费）"),
    代收违约金("4104", "代收违约金"),
    
    自收趸交费("4201", "自收趸交费"), 
    自收服务费("4202", "自收服务费（期缴）"), 
    自收保费("4203", "自收保费（期缴费）"),
    自收违约金("4204", "自收违约金");

    /** value */
    private String value;

    /** code */
    private String code;

    private WMSubjTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}