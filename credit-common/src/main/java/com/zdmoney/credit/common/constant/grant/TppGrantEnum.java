package com.zdmoney.credit.common.constant.grant;

/**
 * tpp放款报盘状态枚举
 * @author YM10104
 *
 */
public enum TppGrantEnum {

	未报盘("01", "未报盘"), 
	已报盘("02", "已报盘"), 
    放款成功("03", "放款成功"), 
    放款失败("04", "放款失败");
    private String code;

    private String value;

    private TppGrantEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
