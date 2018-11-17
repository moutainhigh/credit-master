package com.zdmoney.credit.common.constant;

/**
 * 特殊还款表状态
 * @author 00232949
 *
 */
public enum SpecialRepaymentStateEnum {
	
	申请("申请"),
	审批("审批"),
	通过("通过"),
	拒绝("拒绝"),
	取消("取消"),
	结束("结束"),
	
	//结算单打印相关
	申请结算单("申请结算单"),
	撤销结算单("撤销结算单"),
	结算单已打印("结算单已打印"),
	区域总审批("区域总审批"),
	信贷综合管理部审批("信贷综合管理部审批"),
	信贷综合管理部经理审批("信贷综合管理部经理审批"),
	分管领导审批("分管领导审批"),
	总经理审批("总经理审批");
	
	/** value*/
	private String value;
	
	private SpecialRepaymentStateEnum(String value) {
		this.value = value;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
