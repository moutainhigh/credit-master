package com.zdmoney.credit.common.constant;

public enum UploadFtpAddressType {

	数信("abs"),
	龙信小贷("lxxd"),
	
	待上传("01","待上传"),
	上传失败("02","上传失败"),
	上传成功("03","上传成功");
	private String addressType;
	
	private String uploadStatus;
	
	private String uploadValue;
	
	UploadFtpAddressType(String addressType){
		this.addressType = addressType;
	}

	UploadFtpAddressType(String uploadStatus, String uploadValue){
		this.uploadStatus = uploadStatus;
		this.uploadValue = uploadValue;
	}
	
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getUploadValue() {
		return uploadValue;
	}

	public void setUploadValue(String uploadValue) {
		this.uploadValue = uploadValue;
	}
	
	
}
