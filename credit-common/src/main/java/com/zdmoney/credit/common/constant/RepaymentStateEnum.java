package com.zdmoney.credit.common.constant;

public enum RepaymentStateEnum {
	
	未还款("00001"),
	结清("00002"),
	不足额还款("00003"),
	部分免息结清("00004"),
	不足罚息("00005");
	
	/**枚举key值**/
	private String valueName;
	
	RepaymentStateEnum(String valueName) {
		this.valueName = valueName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	
	
}
