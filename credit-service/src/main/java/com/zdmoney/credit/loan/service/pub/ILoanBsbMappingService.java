package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.LoanBsbMapping;

public interface ILoanBsbMappingService {

	public LoanBsbMapping getByLoanId(long loanId);
	
	public LoanBsbMapping getByOrderNo(String orderNo);
	
	public LoanBsbMapping getByBusNumber(String busNumber);
}
