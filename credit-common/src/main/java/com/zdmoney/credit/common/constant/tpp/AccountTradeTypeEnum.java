package com.zdmoney.credit.common.constant.tpp;
/**
 * 陆金所对公虚拟账户交易类型
 * @author YM10104
 *
 */
public enum AccountTradeTypeEnum {
	充值("0","充值"),
	提现("1","提现"),
	还款("2","还款"),
	垫付("3","垫付"),
	回购("4","回购");
	/** value*/
	private String value;
	
	/** code*/
	private String code;
	private AccountTradeTypeEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 跟据index 获得枚举
	 * @param index
	 * @return
	 */
	public static AccountTradeTypeEnum get(String index){
		for (AccountTradeTypeEnum accountTradeTypeEnum : AccountTradeTypeEnum.values()) {
			if (accountTradeTypeEnum.getCode().equals(index)) {
				return accountTradeTypeEnum;
			}
		}
		return null;
	}
}
