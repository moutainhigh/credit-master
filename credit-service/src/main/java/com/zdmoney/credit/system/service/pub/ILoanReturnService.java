package com.zdmoney.credit.system.service.pub;

import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanReturn;
import com.zdmoney.credit.loan.domain.VLoanInfo;


public interface ILoanReturnService {
	public void insertLoanReturn(LoanReturn loanReturn );
	
	public int countLoanReturn(LoanReturn loanReturn );

	public void saveBuyBackLoanAndUpdateLoan(VLoanInfo vloanInfo, Map<String, String> map);
	
}
