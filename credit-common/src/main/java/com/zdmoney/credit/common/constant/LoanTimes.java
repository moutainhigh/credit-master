package com.zdmoney.credit.common.constant;

public enum LoanTimes {
	六期(6),
	十二期(12),
	十八期(18),
	二十四期(24),
	三十六期(36);
	
	LoanTimes(int value){
		this.value=value;
	}
	
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
