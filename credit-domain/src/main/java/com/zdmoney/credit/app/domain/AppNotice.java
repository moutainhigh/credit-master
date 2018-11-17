package com.zdmoney.credit.app.domain;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * App端公告
 * 
 * @author Ivan
 *
 */
public class AppNotice extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3185428423643099377L;
	/** 主键 **/
	private Long id;
	/** 标题 **/
	private String title;
	/** 内容 **/
	private String content;
	/** 录入时间 **/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date inputTime;
	/** 是否有效 0.无效 1.有效 **/
	private String isValid;
	/** 通知类型 展业 贷后 **/
	private String noticeType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
}