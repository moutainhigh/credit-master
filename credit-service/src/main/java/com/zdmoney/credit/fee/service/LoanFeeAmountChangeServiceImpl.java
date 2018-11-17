package com.zdmoney.credit.fee.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeAmountChangeDao;
import com.zdmoney.credit.fee.domain.LoanFeeAmountChange;
import com.zdmoney.credit.fee.service.pub.ILoanFeeAmountChangeService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款收费导入数据 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeAmountChangeServiceImpl implements ILoanFeeAmountChangeService {

	protected static Log logger = LogFactory.getLog(LoanFeeAmountChangeServiceImpl.class);

	@Autowired
	@Qualifier("loanFeeAmountChangeDaoImpl")
	ILoanFeeAmountChangeDao loanFeeAmountChangeDaoImpl;

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Override
	public LoanFeeAmountChange insert(LoanFeeAmountChange loanFeeAmountChange) {
		if (Strings.isEmpty(loanFeeAmountChange.getId())) {
			loanFeeAmountChange.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_AMOUNT_CHANGE));
		}
		return loanFeeAmountChangeDaoImpl.insert(loanFeeAmountChange);
	}

}
