package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;

public interface ILoanRepaymentDetailLufaxDao  extends IBaseDao<LoanRepaymentDetailLufax>{
	/**
	 * 根据loanId查询还款计划
	 * @param loanId
	 * @return
	 */
	public List<LoanRepaymentDetailLufax> findByLoanId(Long loanId);
	
	/**
	 * 更新剩余的还款计划  根据期数和loanId
	 * @param map
	 */
	public void updateRepaymentDetailLufaxByLoanId(Map<String,Object> map);

	public void deleteByLoanId(Long loanId);
}
