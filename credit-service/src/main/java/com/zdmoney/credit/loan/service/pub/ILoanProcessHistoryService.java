package com.zdmoney.credit.loan.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;

public interface ILoanProcessHistoryService {

	public void insert(LoanProcessHistory loanProcessHistory);
	public Pager searchLoanProcessHistoryWithPg(Map<String, Object> paramMap);
	public Pager searchapprovalWithPg(Map<String, Object> paramMap);
}
