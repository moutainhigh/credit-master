package com.zdmoney.credit.tytx.vo;

import java.io.Serializable;

/**
 * 消息体 统一通讯平台
 * 
 * @author Ivan
 *
 */
public class MsgBodyVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主题 **/
	private String subject = "";
	/** 摘要 **/
	private String summary = "";
	/** 正文内容 **/
	private String body = "";
	/** 邮件发送人 **/
	private String mailFrom = "";
	/** 邮件抄送地址 **/
	private String cc = "";
	/** 邮件暗送地址 **/
	private String bcc = "";
	/** 正文内容格式类型,分别为text、html、url **/
	private String bodyFormat = "";
	/** 消息类型 **/
	private String messageType = "";
	/** 消息是否置顶(0,1) **/
	private String isTop = "";
	/** 消息发布时间 **/
	private String publishTime = "";
	/** 备用字段1 **/
	private String ext1 = "";
	/** 备用字段2 **/
	private String ext2 = "";
	/** 备用字段3 **/
	private String ext3 = "";

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getBodyFormat() {
		return bodyFormat;
	}

	public void setBodyFormat(String bodyFormat) {
		this.bodyFormat = bodyFormat;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

}
