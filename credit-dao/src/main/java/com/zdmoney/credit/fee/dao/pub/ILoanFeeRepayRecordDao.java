package com.zdmoney.credit.fee.dao.pub;

import java.util.Map;

import com.zdmoney.credit.fee.domain.LoanFeeRepayRecord;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface ILoanFeeRepayRecordDao extends IBaseDao<LoanFeeRepayRecord> {
	public LoanFeeRepayRecord selectLoanFeeRepayRecordByFeeIdAndAcctTitle(Map<String,Object> paramMap);
}
