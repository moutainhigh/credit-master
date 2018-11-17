package com.zdmoney.credit.tytx.vo;

import java.io.Serializable;

/**
 * 接收用户明细 统一通讯平台
 * @author fuhongxing
 * @date 2016年7月26日
 * @version 1.0.0
 */
public class CustInfoVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 客户类型: 1员工 3交易用户 10体验用户 11潜在客户-1匿名用户 **/
	private String cType = "-1";
	/**
	 * 客户标识类型: 1手机号码2邮箱地址3接收用户唯一标识(客户号、工号、昵称等) 4 Android手机设备号 5 IOS手机设备号 6 微信号
	 **/
	private String cMarkType = "1";
	/** 标识信息,手机号码、邮箱地址、接收用户唯一标识、手机设备号、微信号 **/
	private String markInfo = "";

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getcMarkType() {
		return cMarkType;
	}

	public void setcMarkType(String cMarkType) {
		this.cMarkType = cMarkType;
	}

	public String getMarkInfo() {
		return markInfo;
	}

	public void setMarkInfo(String markInfo) {
		this.markInfo = markInfo;
	}

}
