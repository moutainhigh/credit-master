package com.zdmoney.credit.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentLevelHisDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentLevelHis;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentLevelHisService;

@Service
public class LoanRepaymentLevelHisServiceImpl implements ILoanRepaymentLevelHisService{
	
	@Autowired
    private ILoanRepaymentLevelHisDao loanRepaymentLevelHisDao;

	@Override
	public void create(LoanRepaymentLevelHis loanRepaymentLevelHis) {
		loanRepaymentLevelHisDao.insert(loanRepaymentLevelHis);
	}

}
