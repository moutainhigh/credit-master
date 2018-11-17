package com.zdmoney.credit.loan.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanLedgerDao;
import com.zdmoney.credit.loan.domain.LoanLedger;

@Repository
public class LoanLedgerDaoImpl extends BaseDaoImpl<LoanLedger> implements ILoanLedgerDao{
	
	private static final String FINDBYLOANID = ".findByLoanId";
	private static final String FINDBYACCOUNT = ".findByAccount";

	@Override
	public List<LoanLedger> getLoanLedgerHasBalanceByDate(int promiseReturnDate) {
		
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("promiseReturnDate", promiseReturnDate);
		
		List<LoanLedger> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getLoanLedgerHasBalanceByDate",map);
		return list;
	}

	@Override
	public LoanLedger findByLoanId(Long id) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+FINDBYLOANID,id);
	}

	@Override
	public LoanLedger findByAccount(String account) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+FINDBYACCOUNT,account);
	}

	@Override
	public LoanLedger findByLoanIdForUpdate(Long id) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+FINDBYLOANID+"ForUpdate",id);
	}

	@Override
	public LoanLedger findByAccountForUpdate(String account) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+FINDBYACCOUNT+"ForUpdate",account);
	}

	@Override
	public int updateOtherPayableByLoanId(LoanLedger loanLedger) {
		User user = UserContext.getUser();
		if (user == null) {
			loanLedger.setUpdator("admin");
		} else {
			loanLedger.setUpdator(user.getName());
		}
		loanLedger.setUpdateTime(new Date());
		return getSqlSession().update(getIbatisMapperNameSpace() + ".updateOtherPayableByLoanId", loanLedger);
	}

	public List<LoanLedger> getLoanLedgerInfoByLoanBelong(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getLoanLedgerInfoByLoanBelong",params);
	}

	@Override
	public List<LoanLedger> findMoreThanZero() {	
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findMoreThanZero");
	}
}
