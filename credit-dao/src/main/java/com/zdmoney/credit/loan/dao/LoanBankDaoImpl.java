package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.domain.LoanBank;

import java.util.Map;


@Repository
public class LoanBankDaoImpl extends BaseDaoImpl<LoanBank> implements ILoanBankDao{
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYACCOUNT = ".findByAccount";
	
	/**
	 * 根据银行卡号查找
	 * @param account
	 * @return
	 */
	@Override
	public LoanBank findByAccount(String account) {
		LoanBank bank = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYACCOUNT, account);
		return bank;
	}

	@Override
	public String findNumByLoanId(Long loanId) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findNumByLoanId", loanId);
	}

	@Override
	public LoanBank findLoanBankByWm3(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanBankByWm3",params);
	}
}
