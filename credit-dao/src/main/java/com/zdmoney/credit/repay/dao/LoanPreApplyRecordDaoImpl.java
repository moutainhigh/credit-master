package com.zdmoney.credit.repay.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.ILoanPreApplyRecordDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;

@Repository
public class LoanPreApplyRecordDaoImpl extends BaseDaoImpl<LoanPreApplyRecord> implements ILoanPreApplyRecordDao{

	@Override
	public LoanPreApplyRecord findByMap(Map<String, Object> maps) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByMap", maps);
	}
	
}