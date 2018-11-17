package com.zdmoney.credit.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;

@Service
public class LoanBsbMappingServiceImpl implements ILoanBsbMappingService{
	
	@Autowired
	ILoanBsbMappingDao loanBsbMappingDao;

	@Override
	public LoanBsbMapping getByLoanId(long loanId) {
		return loanBsbMappingDao.getByLoanId(loanId);
	}

	@Override
	public LoanBsbMapping getByOrderNo(String orderNo) {
		return loanBsbMappingDao.getByOrderNo(orderNo);
	}

	@Override
	public LoanBsbMapping getByBusNumber(String busNumber) {
		return loanBsbMappingDao.getByBusNumber(busNumber);
	}
	
}