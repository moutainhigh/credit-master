package com.zdmoney.credit.common.constant;

/**
 * Created by ym10094 on 2017/5/16.
 */
public enum SpecialRepaymentApplyTeyps {
    一般减免("01","一般减免"),
    结清减免("02","结清减免")
    ;

    private String code;
    private String value;

    SpecialRepaymentApplyTeyps(String code, String value) {
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

    public static  SpecialRepaymentApplyTeyps getValueByCode(String code) {
        for (SpecialRepaymentApplyTeyps applyTeyp:SpecialRepaymentApplyTeyps.values()) {
            if (applyTeyp.getCode().equals(code)) {
                return applyTeyp;
            }
        }
        return null;
    }
}
