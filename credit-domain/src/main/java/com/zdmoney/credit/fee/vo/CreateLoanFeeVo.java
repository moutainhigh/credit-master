package com.zdmoney.credit.fee.vo;

/**
 * 创建服务费主记录 Vo
 * 
 * @author Ivan
 *
 */
public class CreateLoanFeeVo {
	/** 债权编号 **/
	private Long loanId;

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	
	public CreateLoanFeeVo() {
		super();
	}

	public CreateLoanFeeVo(Long loanId) {
		super();
		this.loanId = loanId;
	}
	
}
