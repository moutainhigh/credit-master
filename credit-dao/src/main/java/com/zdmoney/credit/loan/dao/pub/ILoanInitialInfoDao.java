package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;

public interface ILoanInitialInfoDao extends IBaseDao<LoanInitialInfo>{

	/**
	 * 根据loanid查找
	 * @param id
	 * @return
	 */
	LoanInitialInfo findByLoanId(Long id);
	
	/**
	 * 根据appNo查找
	 * @param appNo
	 * @return
	 */
	LoanInitialInfo findByAppNo(String appNo);

}
