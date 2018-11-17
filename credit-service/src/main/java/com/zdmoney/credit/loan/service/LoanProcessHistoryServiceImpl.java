package com.zdmoney.credit.loan.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanProcessHistoryDao;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;
import com.zdmoney.credit.loan.service.pub.ILoanProcessHistoryService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
@Service
public class LoanProcessHistoryServiceImpl implements
		ILoanProcessHistoryService {
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanProcessHistoryDao loanProcessHistoryDaoImpl;
	@Override
	public void insert(LoanProcessHistory loanProcessHistory) {
		loanProcessHistory.setId(sequencesService.getSequences(SequencesEnum.LOAN_PROCESS_HISTORY));
		loanProcessHistoryDaoImpl.insert(loanProcessHistory);
	}

	@Override
	public Pager searchLoanProcessHistoryWithPg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return loanProcessHistoryDaoImpl.searchLoanProcessHistoryWithPg(paramMap);
	}
	
	@Override
	public Pager searchapprovalWithPg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return loanProcessHistoryDaoImpl.searchapprovalWithPg(paramMap);
	}
}
