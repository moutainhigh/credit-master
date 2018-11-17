package com.zdmoney.credit.loan.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentStateDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentState;

@Repository
public class LoanRepaymentStateDaoImpl extends BaseDaoImpl<LoanRepaymentState> implements ILoanRepaymentStateDao{

	@Override
	public List<Long> findHisLoans(Date currDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currDate", currDate);
		return this.getSqlSession().selectList(getIbatisMapperNameSpace() + ".findHisLoans",params);
	}

	@Override
	public Long findReportTerm(Long loanId, Date currDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currDate", currDate);
		params.put("loanId", loanId);
		return this.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findReportTerm",params);
		
	}

	@Override
	public Date findFinishDate(Long loanId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanId", loanId);
		return this.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findFinishDate",params);
	}

	@Override
	public Date findOverdueDate(Long loanId, Date currDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currDate", currDate);
		params.put("loanId", loanId);
		return this.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findOverdueDate",params);
	}

	@Override
	public Long findRepayTerm(Long loanId, Date currDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currDate", currDate);
		params.put("loanId", loanId);
		return this.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findRepayTerm",params);
	}

	@Override
	public int deleteByReportMonth(String reportMonth) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reportMonth", reportMonth);
		return this.getSqlSession().delete(getIbatisMapperNameSpace() + ".deleteByReportMonth",params);
	}

}
