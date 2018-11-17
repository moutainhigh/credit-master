package com.zdmoney.credit.loan.service.pub;

import java.util.Date;
import java.util.List;

import com.zdmoney.credit.loan.domain.LoanPreSettle;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * 结清记录service
 * @author 00232949
 *
 */
public interface ILoanPreSettleService {

	/**
	 * 创建结清记录
	 * @param loanInfo
	 * @param tradeDate
	 */
	void creartPreSettle(VLoanInfo loanInfo, Date tradeDate);

	/**
	 * 查询结清记录
	 * @param loanId
	 */
	List<LoanPreSettle> findByLoanId(Long loanId);

	/**
	 * 还款日创建结清记录
	 * @param loanInfo
	 * @param tradeDate
	 */
	void creartPreSettlePromise(VLoanInfo loanInfo, Date tradeDate);

	/**
	 * 更新结清记录
	 * @param loanPreSettle
	 */
	void updateLoanPreSettle(LoanPreSettle loanPreSettle);
}
