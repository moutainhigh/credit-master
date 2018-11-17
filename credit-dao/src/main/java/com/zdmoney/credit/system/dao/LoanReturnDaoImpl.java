package com.zdmoney.credit.system.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.domain.LoanReturn;
import com.zdmoney.credit.system.dao.pub.ILoanReturnDao;
@Repository
public class LoanReturnDaoImpl extends BaseDaoImpl<LoanReturn> implements ILoanReturnDao{

	@Override
	public int countLoanReturn(LoanReturn loanReturn) {
		int affectNum = 0;
		affectNum=getSqlSession().selectOne(getIbatisMapperNameSpace() + ".countLoanReturn",loanReturn);
		return affectNum;
	}

}
