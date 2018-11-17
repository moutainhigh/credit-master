package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.LoanContract;

public interface ILoanContractService {

	public LoanContract findLoanContractByLoanId(Long loanId);
}
