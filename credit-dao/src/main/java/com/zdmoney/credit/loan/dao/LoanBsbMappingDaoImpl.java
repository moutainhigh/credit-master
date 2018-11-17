package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;


@Repository
public class LoanBsbMappingDaoImpl extends BaseDaoImpl<LoanBsbMapping> implements ILoanBsbMappingDao{
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String GETBYLOANID = ".getByLoanId";
	
	private static final String GETBYORDERNO = ".getByOrderNo";
	
	private static final String GETBYBUSNUMBER = ".selectByBusNumber";
	
	@Override
	public LoanBsbMapping getByLoanId(long loanId) {
		LoanBsbMapping mapping = getSqlSession().selectOne(getIbatisMapperNameSpace() + GETBYLOANID, loanId);
		return mapping;
	}

	@Override
	public LoanBsbMapping getByOrderNo(String orderNo) {
		LoanBsbMapping mapping = getSqlSession().selectOne(getIbatisMapperNameSpace() + GETBYORDERNO, orderNo);
		return mapping;
	}

	@Override
	public LoanBsbMapping getByBusNumber(String busNumber) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + GETBYBUSNUMBER, busNumber);
	}

}
