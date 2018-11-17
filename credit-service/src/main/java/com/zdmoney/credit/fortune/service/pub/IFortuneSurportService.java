package com.zdmoney.credit.fortune.service.pub;

import java.util.Date;

import com.zdmoney.credit.loan.domain.VLoanInfo;

public interface IFortuneSurportService {

	/**
	 * 创建根基loan
	 * @param loanInfo
	 * @param nextRepayDate
	 */
	void createFortuneSurport(VLoanInfo loanInfo, Date nextRepayDate);

}
