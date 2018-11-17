package com.zdmoney.credit.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.ILoanThirdSourceMemoDao;
import com.zdmoney.credit.loan.domain.LoanThirdSourceMemo;
import com.zdmoney.credit.loan.service.pub.ILoanThirdSourceMemoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanThirdSourceMemoServiceImpl implements ILoanThirdSourceMemoService {

	@Autowired
	ILoanThirdSourceMemoDao loanThirdSourceMemoDao;
	@Autowired
	ISequencesService sequencesService;
	
	@Override
	public LoanThirdSourceMemo saveOrUpdate(LoanThirdSourceMemo loanThirdSourceMemo) {
		Long id = loanThirdSourceMemo.getId();
		if (Strings.isEmpty(id)) {
			loanThirdSourceMemo.setId(sequencesService.getSequences(SequencesEnum.LOAN_THIRD_SOURCE_MEMO));
			loanThirdSourceMemoDao.insert(loanThirdSourceMemo);
		} else {
			loanThirdSourceMemoDao.update(loanThirdSourceMemo);
		}
		
		return loanThirdSourceMemo;
	}

}
