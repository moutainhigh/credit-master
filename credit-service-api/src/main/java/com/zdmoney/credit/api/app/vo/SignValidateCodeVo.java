package com.zdmoney.credit.api.app.vo;

import org.hibernate.validator.constraints.NotEmpty;

public class SignValidateCodeVo {

	//验证码
	private String validateCode;
	//验证码类型 1-注册， 2-登录 3-签名验证
	private String validType;
	@NotEmpty(message = "电话号码为空")
	private String mobile;
	@NotEmpty(message = "发送内容参数为空")
	private String content;
	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getValidType() {
		return validType;
	}

	public void setValidType(String validType) {
		this.validType = validType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
