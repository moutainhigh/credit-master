package com.zdmoney.credit.offer.service.pub;

import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;

public interface IOfferService {

	/**
	 * 核心接口开户绑卡
	 * @param personInfo
	 * @param loanBank
	 * @param comEmployee
	 * @return
	 */
	public String openCoreAccount(PersonInfo personInfo, LoanBank loanBank, ComEmployee operator, String bankMobile);
}
