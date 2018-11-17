package com.zdmoney.credit.loan.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.loan.dao.pub.ILoanContractDao;
import com.zdmoney.credit.loan.domain.LoanContract;
import com.zdmoney.credit.loan.service.pub.ILoanContractService;

@Service
public class LoanContractServiceImpl implements ILoanContractService {
	private static final Logger logger = Logger
			.getLogger(EndOfDayServiceImpl.class);
	@Autowired
	private ILoanContractDao loanContractDaoImpl;

	@Override
	public LoanContract findLoanContractByLoanId(Long loanId) {

		return loanContractDaoImpl.findByLoanId(loanId);
	}
}
