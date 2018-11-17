package com.zdmoney.credit.loan.service.pub;


import java.util.Date;
import java.util.List;

import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * @author YM10112
 */
public interface ILoanRepaymentDetailLufaxService {
	
	/**
	 * @param repaymentDetails
	 * @return
	 */
	public List<LoanRepaymentDetailLufax> createLufaxRepayment(List<LoanRepaymentDetail> repaymentDetails);
	/**
	 * 得到指定还款日期的还款计划
	 * @param loanInfo
	 * @param lastRepayDate
	 * @return
	 */
	public LoanRepaymentDetailLufax findByLoanAndReturnDate(VLoanInfo loanInfo,Date lastRepayDate);
	/**
	 * 创建
	 * @param c
	 * @return
	 */
	public LoanRepaymentDetailLufax createLufaxRepaymentDetail( LoanRepaymentDetail repaymentDetail);

	/**
	 * 推送 逾期还款计划信息至陆金所
	 */
	public boolean pushOverdueRepaymentPlan();

	/**
	 * 删除还款计划
	 * @param id
	 */
	public void deleteByLoanId(Long loanId);


}
