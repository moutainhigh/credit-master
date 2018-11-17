package com.zdmoney.credit.common.constant;


public enum LoanStateEnum {
	申请("申请"),
	审核中("审核中"),
	通过("通过"),
	拒绝("拒绝"),
	退回("退回"),
	取消("取消"),
	正常("正常"),
	逾期("逾期"),
	结清("结清"),
	坏账("坏账"),
	预结清("预结清"),
	正常费用减免("正常费用减免");
	
	/** value*/
	private String value;
	
	private LoanStateEnum( String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
