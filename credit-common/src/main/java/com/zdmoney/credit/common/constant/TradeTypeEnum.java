package com.zdmoney.credit.common.constant;

public enum TradeTypeEnum{
	现金("现金"),
	转账("转账"),
	快捷通("快捷通"),
	通联代扣("通联代扣"),
	富友代扣("富友代扣"),
	上海银联代扣("上海银联代扣"),
	上海银联支付("上海银联支付"),
	冲正补记("冲正补记"),
	冲正("冲正"),
	挂账("挂账"),
	保证金("保证金"),
	系统使用保证金("系统使用保证金"),
	风险金("风险金"),
	宝付("宝付"),
	银生宝("银生宝");
	

	/** value*/
	private String value;
	
	
	private TradeTypeEnum( String value) {
		this.value = value;
	}

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
