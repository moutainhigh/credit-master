package com.zdmoney.credit.common.constant.tpp;

public enum TppRealPaySysNoEnum {
	通联代扣("0","通联代扣"),
	上海银联支付("4","上海银联支付"),
	用友支付("6","用友支付"),
	爱特代扣("10","爱特代扣"),
	快捷通("18","快捷通"),
	陆金所("00021","陆金所"),
	宝付("20","宝付"),
	银生宝("22","银生宝");
	/** value*/
	private String value;
	
	/** code*/
	private String code;
	private TppRealPaySysNoEnum(String code, String value) {
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
	public static TppPaySysNoEnum get(String index){
		for (TppPaySysNoEnum tppPaySysNoEnum : TppPaySysNoEnum.values()) {
			if (tppPaySysNoEnum.getCode().equals(index)) {
				return tppPaySysNoEnum;
			}
		}
		return null;
	}
}
