package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;

public interface ILoanBsbMappingDao extends IBaseDao<LoanBsbMapping>{
	
	/**
	 * 
	 * @param loanId
	 * @return
	 */
	public LoanBsbMapping getByLoanId(long loanId);
	
	/**
	 * 
	 * @param orderNo
	 * @return
	 */
	public LoanBsbMapping getByOrderNo(String orderNo);
	
	/**
	 * 
	 * @param busNumber
	 * @return
	 */
	public LoanBsbMapping getByBusNumber(String busNumber);
}
