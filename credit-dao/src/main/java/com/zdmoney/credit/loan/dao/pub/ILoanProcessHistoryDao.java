package com.zdmoney.credit.loan.dao.pub;


import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;

public interface ILoanProcessHistoryDao extends IBaseDao<LoanProcessHistory>{

	Pager searchLoanProcessHistoryWithPg(Map<String, Object> paramMap);

	Pager searchapprovalWithPg(Map<String, Object> paramMap);
	
}