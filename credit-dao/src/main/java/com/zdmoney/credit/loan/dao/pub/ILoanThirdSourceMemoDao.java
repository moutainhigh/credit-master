package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanThirdSourceMemo;

public interface ILoanThirdSourceMemoDao extends IBaseDao<LoanThirdSourceMemo> {

	public LoanThirdSourceMemo findByOfferRepayInfoId(Long offerRepayInfoId);
}
