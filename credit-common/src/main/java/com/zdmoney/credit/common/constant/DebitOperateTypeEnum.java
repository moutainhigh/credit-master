package com.zdmoney.credit.common.constant;

/**
 * 划扣类型
 */
public enum DebitOperateTypeEnum {

	自动代扣("1", "自动代扣"), // 当期提前还款、当期正常还款、对公委托还款
	重试代扣("2", "重试代扣"), // 第一次不为全额代扣，重新发起第二次代扣
	逾期代扣("3", "逾期代扣"), 
	追偿代扣("4", "追偿代扣"),
	收费代扣("5", "收费代扣"),
	提前还款代扣("6", "提前还款代扣"), // 提前一次性结清
	手工还款代扣("7", "手工还款代扣"),
	当期代偿("8", "当期代偿"), // 逾期代偿
	当期追偿("9", "当期追偿"),
	当期提前还款("10", "当期提前还款"); // 暂不使用

    private String code;

    private String value;

    private DebitOperateTypeEnum(String code, String value) {
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
