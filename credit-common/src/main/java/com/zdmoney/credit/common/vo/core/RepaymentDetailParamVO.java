package com.zdmoney.credit.common.vo.core;

/**
 * 还款计划接口参数VO
 * 
 * @author 00235304
 * 
 */
public class RepaymentDetailParamVO {

	private String userCode;

	private Long loanId;

	private Long max;

	private Long offset;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "RepaymentDetailParamVO [userCode=" + userCode + ", loanId="
				+ loanId + ", max=" + max + ", offset=" + offset + "]";
	}

}
