package com.zdmoney.credit.fee.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeRepayRecordDao;
import com.zdmoney.credit.fee.domain.LoanFeeRepayRecord;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class LoanFeeRepayRecordDaoImpl extends BaseDaoImpl<LoanFeeRepayRecord>
		implements ILoanFeeRepayRecordDao {

	@Override
	public LoanFeeRepayRecord selectLoanFeeRepayRecordByFeeIdAndAcctTitle(
			Map<String,Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".selectLoanFeeRepayRecordByFeeIdAndAcctTitle", paramMap);
	}

	
}
