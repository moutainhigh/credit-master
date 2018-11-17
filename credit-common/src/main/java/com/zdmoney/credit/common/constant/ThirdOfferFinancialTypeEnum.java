package com.zdmoney.credit.common.constant;

/**
 * 第三方代付报盘财务类型
 * @author 00236640
 */
public enum ThirdOfferFinancialTypeEnum {

    /** 报盘划扣放款本金 **/
    放款本金("FKBJ"),
    /** 报盘划扣手续费 **/
    手续费("SXF");

    private String value;

    private ThirdOfferFinancialTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
