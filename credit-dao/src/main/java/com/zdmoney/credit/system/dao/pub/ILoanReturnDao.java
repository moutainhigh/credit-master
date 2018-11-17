package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanReturn;

public interface ILoanReturnDao extends IBaseDao<LoanReturn>{
	public int countLoanReturn(LoanReturn loanReturn); 
}
