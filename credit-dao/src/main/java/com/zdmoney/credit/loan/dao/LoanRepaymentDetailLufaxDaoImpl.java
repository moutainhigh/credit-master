package com.zdmoney.credit.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailLufaxDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;

import org.springframework.stereotype.Repository;


@Repository
public class LoanRepaymentDetailLufaxDaoImpl extends BaseDaoImpl<LoanRepaymentDetailLufax> implements ILoanRepaymentDetailLufaxDao{

	@Override
	public List<LoanRepaymentDetailLufax> findByLoanId(Long loanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		List<LoanRepaymentDetailLufax> repaymentDetails = findListByMap(paramMap);
		return repaymentDetails;
	}

	@Override
	public void updateRepaymentDetailLufaxByLoanId(Map<String, Object> map) {
		getSqlSession().update(getIbatisMapperNameSpace() + ".updateRepaymentDetailLufaxByLoanId", map);
	}

	@Override
	public void deleteByLoanId(Long loanId) {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".deleteByLoanId", loanId);
	}
}
