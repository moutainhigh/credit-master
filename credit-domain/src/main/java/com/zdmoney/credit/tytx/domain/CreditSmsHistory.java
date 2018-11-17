package com.zdmoney.credit.tytx.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 短信发送记录实体
 * @author fuhongxing
 * @date 2016年7月26日
 * @version 1.0.0
 */
public class CreditSmsHistory extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
    
    private String sender; //发送人

    private String mobile; //手机号码
    
    private String operationMethod;//请求接口方法
    
	private String requestContent;//请求传递数据
	
	private String responseContent; //响应内容
    
	private Date requestDate;//请求系统开始时间
	
	private Date responseDate;//响应返回数据时间

    private String remark;	//备注

    private String status; //状态
    
    private String type; //类型
    
    private String employeeId; //标识员工(区分离职短信发送是在哪个员工客户下发送的)
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOperationMethod() {
		return operationMethod;
	}

	public void setOperationMethod(String operationMethod) {
		this.operationMethod = operationMethod;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
    
   
}