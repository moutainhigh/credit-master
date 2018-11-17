package com.zdmoney.credit.fee.vo;

import com.zdmoney.credit.fee.domain.LoanFeeOffer;
/**
 * 服务费报盘查询vo
 * @author user
 *
 */
public class LoanFeeOfferVo extends LoanFeeOffer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5393895168816540259L;

	/** 收取状态 **/
	private String  loanFeeState;
	
	/** 操作人**/
	private String  creator;

	public String getLoanFeeState() {
		return loanFeeState;
	}

	public void setLoanFeeState(String loanFeeState) {
		this.loanFeeState = loanFeeState;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	
}
