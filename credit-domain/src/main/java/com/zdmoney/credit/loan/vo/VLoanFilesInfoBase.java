package com.zdmoney.credit.loan.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

/**
 * 客户档案信息
 * @author 00236633
 */
public class VLoanFilesInfoBase {

	@NotNull(message="借款id不能为空")
	@Range(min=1,max=999999999999999999l,message="借款id必须大于零,小于1000000000000000000")
	private Long loanId;

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
}
