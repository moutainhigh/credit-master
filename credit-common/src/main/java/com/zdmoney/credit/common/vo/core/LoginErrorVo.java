package com.zdmoney.credit.common.vo.core;

import java.util.Date;

public class LoginErrorVo {
	/**
	 * 失败次数
	 */
	private int errorCount;
	
	/**
	 * 锁定开始时间
	 */
	private Date lockStartTime;

	/**
	 * 
	 *  最近登录时间
	 */
	private Date recentLoginTime;
	
	/**
	 * 
	 * 工号
	 */
	private String userCode;
	

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	

	public Date getLockStartTime() {
		return lockStartTime;
	}

	public void setLockStartTime(Date lockStartTime) {
		this.lockStartTime = lockStartTime;
	}

	public Date getRecentLoginTime() {
		return recentLoginTime;
	}

	public void setRecentLoginTime(Date recentLoginTime) {
		this.recentLoginTime = recentLoginTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Override
	public String toString() {
		return "LoginErrorVo:{errorCount="+errorCount+",lockStartTime="+lockStartTime+",recentLoginTime="+recentLoginTime+",userCode=" + userCode+"}" ;
	}
	

}
