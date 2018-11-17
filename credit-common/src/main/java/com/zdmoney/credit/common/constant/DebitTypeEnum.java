package com.zdmoney.credit.common.constant;

public enum DebitTypeEnum {
    
    P0_A("P0_A"), // 正常且还款日是1号
    P0_B("P0_B"), // 正常且还款日是16号
    P1_A("P1_A"), // 逾期起始日等于T-0月16日
    P1_B("P1_B"), // 逾期起始日等于T-0月01日
    P2_A("P2_A"), // 逾期起始日等于T-1月16日
    P2_B("P2_B"), // 逾期起始日等于T-1月01日
    P2_C("P2_C"), // 逾期起始日等于T-2月16日
    P3_A("P3_A"), // 逾期起始日等于T-2月01日
    P3_B("P3_B"), // 逾期起始日等于T-3月16日
    PX_A("PX_A"), // 逾期起始日小于等于T-3月01日且还款日是1号
    PX_B("PX_B"); // 逾期起始日小于等于T-4月16日且还款日是16号
    
    /** value*/
    private String value;
    
    private DebitTypeEnum( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
