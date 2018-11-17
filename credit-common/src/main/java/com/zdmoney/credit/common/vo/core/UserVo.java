package com.zdmoney.credit.common.vo.core;

public class UserVo {
	
	private String authKey;
	private String userCode;
	private String newPassword;
	
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String toString() {
		return "UserVo [authKey=" + authKey + ", userCode="
				+ userCode + ", password=" + newPassword+ "]";
	}
}
