package com.zdmoney.credit.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.loan.dao.pub.ILoanTrustGrantInfoDao;
import com.zdmoney.credit.loan.domain.LoanTrustGrantInfo;
import com.zdmoney.credit.loan.service.pub.ILoanTrustGrantInfoService;


@Service
@Transactional
public class LoanTrustGrantInfoServiceImpl implements ILoanTrustGrantInfoService{

	@Autowired
	ILoanTrustGrantInfoDao loanTrustGrantInfoDao;

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public LoanTrustGrantInfo saveNow(LoanTrustGrantInfo trustGrantInfo) {
		return loanTrustGrantInfoDao.insert(trustGrantInfo);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int updateNow(LoanTrustGrantInfo trustGrantInfo) {
		return loanTrustGrantInfoDao.update(trustGrantInfo);
	}
	
	
	
}
