package com.zdmoney.credit.loan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.loan.dao.pub.ILoanPreSettleDao;
import com.zdmoney.credit.loan.domain.LoanPreSettle;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanPreSettleService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanPreSettleServiceImpl implements ILoanPreSettleService{

	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanPreSettleDao loanPreSettleDao;
	
	
	@Override
	public void creartPreSettle(VLoanInfo loanInfo, Date tradeDate) {
//		new PreSettle(loan: loanInfo,settleDate:repay.riTradeDate,curDate: new Date()).save();
		LoanPreSettle loanPreSettle = new LoanPreSettle();
		loanPreSettle.setId(sequencesService.getSequences(SequencesEnum.LOAN_PRE_SETTLE));
		loanPreSettle.setCreateTime(new Date());
		loanPreSettle.setLoanId(loanInfo.getId());
		loanPreSettle.setSettleDate(tradeDate);
		loanPreSettleDao.insert(loanPreSettle);
	}

	@Override
	public List<LoanPreSettle> findByLoanId(Long loanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanId",loanId);
		return loanPreSettleDao.findListByMap(map);
	}

	@Override
	public void creartPreSettlePromise(VLoanInfo loanInfo, Date tradeDate) {
		LoanPreSettle loanPreSettle = new LoanPreSettle();
		loanPreSettle.setId(sequencesService.getSequences(SequencesEnum.LOAN_PRE_SETTLE));
		loanPreSettle.setCreateTime(new Date());
		loanPreSettle.setLoanId(loanInfo.getId());
		loanPreSettle.setSettleDate(tradeDate);
		loanPreSettle.setRealSettleDate(tradeDate);
		loanPreSettleDao.insert(loanPreSettle);
	}

	@Override
	public void updateLoanPreSettle(LoanPreSettle loanPreSettle) {
		loanPreSettleDao.update(loanPreSettle);
	}

}
