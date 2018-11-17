/**
 * Copyright (c) 2017, jiaxm@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年2月23日下午4:49:18
 *
*/

package com.zdmoney.credit.common.vo;

import java.io.Serializable;

/**
 * ClassName:returnObject <br/>
 * Date:     2017年2月23日 下午4:49:18 <br/>
 * @author   jiaxm@yuminsoft.com
 */
public class ReturnObject implements Serializable{

    /**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -5384385193328996731L;
	
	/** 响应状态码 **/
    private String resCode;
    /** 响应内容 **/
    private Object resMsg;
    
	public ReturnObject() {
		super();
	}
	public ReturnObject(String resCode, Object resMsg) {
		super();
		this.resCode = resCode;
		this.resMsg = resMsg;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public Object getResMsg() {
		return resMsg;
	}
	public void setResMsg(Object resMsg) {
		this.resMsg = resMsg;
	}
	public void setSuccessMsg(Object msg){
		this.resCode="000000";
		this.resMsg=msg;
	}
	public void setErrorMsg(String resCode,Object msg){
		this.resCode=resCode;
		this.resMsg=msg;
	}
    
}
