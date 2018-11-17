package com.zdmoney.credit.common.constant;

/**
 * 服务费收费状态枚举类
 * @author 00236640
 * @version $Id: LoanFeeStateEnum.java, v 0.1 2016年7月15日 下午1:48:30 00236640 Exp $
 */
public enum LoanFeeStateEnum {
    
    未收取("未收取"), 
    已收取("已收取"), 
    部分收取("部分收取");

    /** value */
    private String value;

    private LoanFeeStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
