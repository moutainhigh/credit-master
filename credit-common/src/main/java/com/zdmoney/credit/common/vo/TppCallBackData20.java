package com.zdmoney.credit.common.vo;

import java.util.Date;
/**
 * 用于封装tpp20回执，xml解析后的数据
 * @author 00232949
 *
 */
public class TppCallBackData20 {
	/**
     * 请求流水
     */
    String requestId;
    /**
     * 任务ID
     */
    String taskId;
    /**
     * 订单编号
     */
    String orderNo;
    
    /**
     * 交易流水号
     */
    String tradeFlow;
    /**
     * 返回代码
     */
    String returnCode;
    /**
     * 返回信息
     */
    String returnInfo;
    
    /**
     * 成功金额
     */
    String  successAmount;

    /**
     * 失败原因
     */
    String  failReason;
    
    /**
     * 接收时间
     */
    Date receiveDate = new Date();

    
    /**
     * 备注
     */
    String msgInId;
    
    /**
     * 划扣通道
     */
    String paySysNo;
   
	/**
     * 备注
     */
    String memo;
    
    /**
     * 商户号
     */
    String merId;
    
    public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getPaySysNo() {
		return paySysNo;
	}
	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTradeFlow() {
		return tradeFlow;
	}
	public void setTradeFlow(String tradeFlow) {
		this.tradeFlow = tradeFlow;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	public String getSuccessAmount() {
		return successAmount;
	}
	public void setSuccessAmount(String successAmount) {
		this.successAmount = successAmount;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getMsgInId() {
		return msgInId;
	}
	public void setMsgInId(String msgInId) {
		this.msgInId = msgInId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
    
	
}
