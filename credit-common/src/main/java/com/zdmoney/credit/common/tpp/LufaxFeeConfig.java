package com.zdmoney.credit.common.tpp;

/**
 * 陆金所 Tpp报盘 配置信息
 */
public class LufaxFeeConfig {
	/** 业务系统编码 **/
	public static final String BIZ_SYS_NO = "004";
	
	/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
	public static final String PAY_SYS_NO = "18";
	
	/** 信息类别编码 陆金所服务费 **/
	public static final String INFO_CATEGORY_CODE = "10082";
}
