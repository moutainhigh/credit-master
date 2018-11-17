package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.LoanTrustGrantInfo;

public interface ILoanTrustGrantInfoService {

	/**
	 * 立即保存，独立事物
	 * @param trustGrantInfo
	 */
	public LoanTrustGrantInfo  saveNow(LoanTrustGrantInfo trustGrantInfo);

	/**
	 * 立即跟新，独立事物
	 * @param trustGrantInfo
	 */
	public int updateNow(LoanTrustGrantInfo trustGrantInfo);

}
