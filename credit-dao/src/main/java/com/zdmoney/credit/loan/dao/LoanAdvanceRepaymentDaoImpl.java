package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanAdvanceRepaymentDao;
import com.zdmoney.credit.loan.domain.LoanAdvanceRepayment;

@Repository
public class LoanAdvanceRepaymentDaoImpl extends BaseDaoImpl<LoanAdvanceRepayment> implements ILoanAdvanceRepaymentDao{

	@Override
	public void insertAdvanceRepaymentJob() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".insertAdvanceRepaymentJob");

	}
}
