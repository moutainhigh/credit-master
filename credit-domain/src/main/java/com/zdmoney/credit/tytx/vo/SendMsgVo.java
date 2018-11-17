package com.zdmoney.credit.tytx.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送短信接口 Vo 统一通讯平台
 * 
 * @author Ivan
 *
 */
public class SendMsgVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 系统号 **/
	private String sysUID = "1014";
	/** 工号 **/
	private String empId = "creditsend";
	/** 用户信息 **/
	private List<CustInfoVo> uInfo = new ArrayList<CustInfoVo>();
	/** 发送策略，只能为以下几个值：0指定通道发送 **/
	private String policy = "0";
	/** 发送渠道 发送策略=0(指定通道发送)：格式为:sms或者mail或inform **/
	private String channel = "sms";
	/** 模版ID **/
	private String templateId = "";
	/** 除通用规则外还可指定自定义规则对信息进行预处理 **/
	private String specialId = "";
	/** 信息类别 **/
	private String mtype = "10000004";
	/** 消息体 **/
	private MsgBodyVo msgBody = null;
	/** 计划发送时间 **/
	private String delaySendTime = "";
	/** 最晚发送时间 **/
	private String lateSendTime = "2099-01-01 23";
	/** 邮件资源对象 **/
	private ResourceInfoVo resource = null;
	/** 字符编码方式 **/
	private String encoding = "UTF-8";

	public String getSysUID() {
		return sysUID;
	}

	public void setSysUID(String sysUID) {
		this.sysUID = sysUID;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public List<CustInfoVo> getuInfo() {
		return uInfo;
	}

	public void setuInfo(List<CustInfoVo> uInfo) {
		this.uInfo = uInfo;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSpecialId() {
		return specialId;
	}

	public void setSpecialId(String specialId) {
		this.specialId = specialId;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public MsgBodyVo getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(MsgBodyVo msgBody) {
		this.msgBody = msgBody;
	}

	public String getDelaySendTime() {
		return delaySendTime;
	}

	public void setDelaySendTime(String delaySendTime) {
		this.delaySendTime = delaySendTime;
	}

	public String getLateSendTime() {
		return lateSendTime;
	}

	public void setLateSendTime(String lateSendTime) {
		this.lateSendTime = lateSendTime;
	}

	public ResourceInfoVo getResource() {
		return resource;
	}

	public void setResource(ResourceInfoVo resource) {
		this.resource = resource;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
