package com.zdmoney.credit.common.constant;

/**
 * 报盘文件状态
 * @author 00232949
 *
 */
public enum OfferStateEnum {
	
	未报盘("未报盘"),
	/**已发送tpp还未回盘*/
	已报盘("已报盘"),
	/**回盘金额和报盘金额相等，生命周期结束*/
	已回盘全额("已回盘全额"),
	/**已回盘，金额不等于报盘金额，包括部分扣款和扣款失败*/
	已回盘非全额("已回盘非全额"),
	/**对于未报盘和部分成功的数据，可以进行关闭，关闭后可以再次生成新报盘*/
	已关闭("已关闭"),

	/**该枚举值[已回盘]注意不要保存至数据库中，仅限于在java程序中与原来程序保持一致时使用*/
	已回盘("已回盘");
	
	/** value*/
	private String value;
	
	private OfferStateEnum( String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
