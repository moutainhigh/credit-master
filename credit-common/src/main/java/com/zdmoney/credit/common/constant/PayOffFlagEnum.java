package com.zdmoney.credit.common.constant;

/**
 * 当期结清标志
 */
public enum PayOffFlagEnum {

	本期未结清("0", "本期未结清"),	           
	正常结清("1", "正常结清"),	           
	逾期结清("2", "逾期结清"),	           
	提前结清("3", "提前结清"),	           
	逾期部分还款("4", "逾期部分还款"),             
	逾期未还款("5", "逾期未还款"),               
	代偿结清("6", "代偿结清"),                 
	代偿后逾期结清("7", "代偿后逾期结清"),           
	买断回购债转结清("8", "买断回购债转结清"),         
	买断回购债转后逾期结清("9", "买断回购债转后逾期结清");   

    private String code;

    private String value;

    private PayOffFlagEnum(String code, String value) {
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
