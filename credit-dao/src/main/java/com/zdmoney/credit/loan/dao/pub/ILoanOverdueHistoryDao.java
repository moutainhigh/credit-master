package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanOverdueHistory;

public interface ILoanOverdueHistoryDao extends IBaseDao<LoanOverdueHistory>{

	/**
	 * 根据loanid查询
	 * @param id
	 * @return
	 */
	LoanOverdueHistory findByLoanId(Long id);

}
