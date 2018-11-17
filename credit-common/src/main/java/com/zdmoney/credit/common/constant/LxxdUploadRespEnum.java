package com.zdmoney.credit.common.constant;

/**
 * 龙信小贷 上传文件响应编码
 * @author 10098  2017年3月6日 下午6:19:22
 */
public enum LxxdUploadRespEnum {

	方法调用异常("U1000","方法调用异常"),
	接口调用成功("U1001","接口调用成功"),
	认证未通过("U1002","认证未通过");	
	private String code;
	private String msg;
	
	private LxxdUploadRespEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
