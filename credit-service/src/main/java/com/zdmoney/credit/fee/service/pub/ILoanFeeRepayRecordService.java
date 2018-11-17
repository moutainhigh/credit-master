package com.zdmoney.credit.fee.service.pub;

import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.domain.LoanFeeRepayRecord;

public interface ILoanFeeRepayRecordService {
	
	public LoanFeeRepayRecord saveLoanFeeRepayRecord(LoanFeeRepayRecord loanFeeRepayRecord);
	
	public void saveLoanFeeSplit(LoanFeeRepayInfo loanFeeRepayInfo);
}	
