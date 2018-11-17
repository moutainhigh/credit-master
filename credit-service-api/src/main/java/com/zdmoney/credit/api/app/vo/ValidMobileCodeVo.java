package com.zdmoney.credit.api.app.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ValidMobileCodeVo {
	@NotEmpty(message = "验证码唯一标识参数为空")
	@Length(max = 30, message = "验证码唯一标识参数超过30个字符")
	private String token;

	@NotEmpty(message = "验证码参数为空")
	@Length(max = 10, message = "验证码参数超过10个字符")
	private String code;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
