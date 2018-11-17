package com.zdmoney.credit.common.constant;

public enum AbsUploadStatusEnum {
	待上传("待上传"),
	成功("成功"),
	失败("失败");
	
	private String status;
	
	private AbsUploadStatusEnum(String status){
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
