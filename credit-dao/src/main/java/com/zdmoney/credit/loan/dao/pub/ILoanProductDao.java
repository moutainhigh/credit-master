package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanProduct;

public interface ILoanProductDao  extends IBaseDao<LoanProduct>{

	/**
	 * 根据loanid查找
	 * @param id
	 * @return
	 */
	LoanProduct findByLoanId(Long loanId);

}
