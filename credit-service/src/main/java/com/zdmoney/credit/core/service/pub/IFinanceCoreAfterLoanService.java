package com.zdmoney.credit.core.service.pub;

import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.system.domain.ComEmployee;

public interface IFinanceCoreAfterLoanService {

	/**
	 * 财务放款信息入库
	 * @param employee
	 * @param loan
	 */
	public void financingGrantLoan(ComEmployee employee, VLoanInfo loan);
}
