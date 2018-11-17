package com.zdmoney.credit.common.constant;

/**
 * 陆金所还款标志 
 * @author YM10112
 *
 */
public enum RepaymentStateLufaxEnum {
	
	已付清("010","已付清"),
	逾期("020","逾期"),
	罚息保费逾期("025","罚息保费逾期"),
	部分还款("030","部分还款"),
	未付("040","未付"),
	提前结清("050","提前结清"),
	代偿中("060","代偿中"),
	代偿结清("070","代偿结清"),
	当期追偿完成("080","当期追偿完成"),
	逾期还款("090","逾期还款"),
	追偿结清("100","追偿结清"),
	当期逾期代垫("110","当期逾期代垫"),
	提前代偿("120","提前代偿"),
	当期逾期保证金代垫("130","当期逾期保证金代垫"),
	保证金代偿结清("140","保证金代偿结清"),
	证大代偿结清("150","证大代偿结清"),
	证大一次性结清("160","证大一次性结清");
	
	/** value*/
	private String value;
	
	/** code*/
	private String code;
	
	private RepaymentStateLufaxEnum(String code, String value) {
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
