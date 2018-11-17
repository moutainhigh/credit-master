package com.zdmoney.credit.common.exception;

/**
 * 回盘的处理异常，如是非主要异常，跳过处理的用此异常封装
 * @author 00232949
 *
 */
public class OfferBackException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6706829863869573609L;

	private String errCode;

	private String message;

	
	public OfferBackException(String message) {
		super();
		this.message = message;
	}


	public String getErrCode() {
		return errCode;
	}


	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
