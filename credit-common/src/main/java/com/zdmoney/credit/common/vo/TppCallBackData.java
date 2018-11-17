package com.zdmoney.credit.common.vo;

import java.util.Date;

/**
 * 用于封装tpp回执，xml解析后的数据
 * @author 00232949
 *
 */
public class TppCallBackData {
	    /**
	     * TPP系统请求编号
	     */
	    String requestCode;
	    /**
	     * 订单编号
	     */
	    String orderNo;
	    /**
	     * 返回代码
	     */
	    String returnCode;
	    /**
	     * 返回信息
	     */
	    String returnInfo;

	    /**
	     * 接收时间
	     */
	    Date receiveDate = new Date();

	    /**
	     * 备注
	     */
	    String msgInId;
	    /**
	     * 备注
	     */
	    String memo;

		public String getRequestCode() {
			return requestCode;
		}

		public void setRequestCode(String requestCode) {
			this.requestCode = requestCode;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
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


		public Date getReceiveDate() {
			return receiveDate;
		}

		public void setReceiveDate(Date receiveDate) {
			this.receiveDate = receiveDate;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getMsgInId() {
			return msgInId;
		}

		public void setMsgInId(String msgInId) {
			this.msgInId = msgInId;
		}
}
