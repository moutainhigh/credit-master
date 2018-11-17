package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanAdvanceRepayment;

public interface ILoanAdvanceRepaymentDao extends IBaseDao<LoanAdvanceRepayment>{
	public void insertAdvanceRepaymentJob();
}
