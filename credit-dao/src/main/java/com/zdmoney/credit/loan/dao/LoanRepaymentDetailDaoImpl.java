package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoanRepaymentDetailDaoImpl extends BaseDaoImpl<LoanRepaymentDetail> implements ILoanRepaymentDetailDao{
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYLOANIDANDREPAYMENTSTATE = ".findByLoanIdAndRepaymentState";
	private static final String FINDBYLOANIDANDREPAYMENTSTATEINSUM = ".findByLoanIdAndRepaymentStateInSum";
	private static final String GETDRAWRISKSUMDEFICIT = ".getDrawRiskSumDeficit";
	private static final String DELETEBYLOANID = ".deleteByLoanId";
	private static final String FINDCURRENTACCRUAL = ".findCurrentAccrual";
	
	/**
	 * 跟据键值信息查询实体集合（通常是组合条件查询）
	 * @return
	 */
	@Override
	public List<LoanRepaymentDetail> findByLoanIdAndRepaymentState(Map<String, Object> map) {
		List<LoanRepaymentDetail> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + FINDBYLOANIDANDREPAYMENTSTATE, map);
		return rstList;
	}

	@Override
	public BigDecimal findByLoanIdAndNotRepaymentStateInSum(Map<String, Object> map) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYLOANIDANDREPAYMENTSTATEINSUM, map);
	}

	@Override
	public BigDecimal getDrawJimuRiskSumDeficit(Map<String, Object> map) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + GETDRAWRISKSUMDEFICIT, map);
	}
	
	@Override
	public BigDecimal getDrawRiskSumDeficit(Map<String, Object> map) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + GETDRAWRISKSUMDEFICIT, map);
	}
	
	@Override
	public int updateYCXJQAllStateToSettlementByLoanId(Long loanId,
			Date tradeDate) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loanId", loanId);
		map.put("tradeDate", tradeDate);
		int affectNum = 0;
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateYCXJQAllStateToSettlementByLoanId", map);
		return affectNum;
	}

	public int updateYCXJQAllDeficitToZeroByLoanId(Long loanId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loanId", loanId);
		int affectNum = 0;
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateYCXJQAllDeficitToZeroByLoanId", map);
		return affectNum;
	}

	/**
	 * 根据loanId查询还款计划
	 * @param loanId
	 * @return
	 */
	@Override
	public List<LoanRepaymentDetail> findByLoanId(Long loanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		List<LoanRepaymentDetail> repaymentDetails = findListByMap(paramMap);
		return repaymentDetails;
	}

	/**
	 * 根据loanId删除还款计划
	 * @param loanId
	 */
	@Override
	public void deleteByLoanId(Long loanId) {
		getSqlSession().delete(getIbatisMapperNameSpace() + DELETEBYLOANID, loanId);
	}

	@Override
	public Pager getRepaymentDatailWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findListWithPGByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".findCountByMap");
		return doPager(pager,paramMap);
	}

	/**
	 * 根据loanId和currentTerm查询
	 * @param loanId
	 * @param currentTerm
	 * @return
	 */
	@Override
	public LoanRepaymentDetail findByLoanIdAndCurrentTerm(Long loanId, Long currentTerm) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("currentTerm", currentTerm);
		List<LoanRepaymentDetail> details = findListByMap(paramMap);
		LoanRepaymentDetail detail = null;
		if (null != details && !details.isEmpty() && details.size() == 1) {
			detail = details.get(0);
		}
		return detail;
	}

	/**
	 * 根据loanId查询当前利息
	 * @param loanId
	 * @return
	 */
	@Override
	public BigDecimal findCurrentAccrual(Long loanId, Long time) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("time", time);
		Object obj = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDCURRENTACCRUAL, paramMap);
		if (obj != null) {
			BigDecimal currentAccrual = new BigDecimal(obj.toString());
			return currentAccrual;
		} else {
			return null;
		}
	}

	@Override
	public LoanRepaymentDetail findRepaymentDetailByLoanAndReturnDate(Map<String, String> map) {
		LoanRepaymentDetail detail = null;
		 detail =getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findRepaymentDetailByLoanAndReturnDate", map);		
		
		return detail;
	}
	
	@Override
	public String findRepaymentLevel(Map<String, Object> map) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findRepaymentLevel", map);
	}

	@Override
	public List<LoanRepaymentDetail> findEqualCurrentTerm(Long loanId, List<Long> currentTerms) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("currentTerms",currentTerms);
		List<LoanRepaymentDetail> repaymentDetails = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findEqualCurrentTerm", paramMap);
		return repaymentDetails;
	}

	@Override
	public List<LoanRepaymentDetail> findTrustRepaymentState(Map<String, Object> paramMap) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findTrustRepaymentState", paramMap);
	}

	@Override
	public List<LoanRepaymentDetail> findBigCurrentTerm(Long loanId, Long currentTerm) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("currentTerm",currentTerm);
		List<LoanRepaymentDetail> repaymentDetails = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findBigCurrentTerm", paramMap);
		return repaymentDetails;
	}

	@Override
	public List<LoanRepaymentDetail> findByLoanBelongAndReturnDate(
			String loanBelong, Date returnDate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanBelong", loanBelong);
		paramMap.put("returnDate",returnDate);
		List<LoanRepaymentDetail> repaymentDetails = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByLoanBelongAndReturnDate", paramMap);
		return repaymentDetails;
	}

	@Override 
	public List<LoanRepaymentDetail> findOverdueCompensatoryToLufax(Map<String, Object> map) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOverdueCompensatoryToLufax",map);
	}

	@Override
	public List<LoanRepaymentDetail> findByLoanIdAndFactReturnDate(Long loanId,
			Date factReturnDate) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("factReturnDate",factReturnDate);
		List<LoanRepaymentDetail> repaymentDetails = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByLoanIdAndFactReturnDate", paramMap);
		return repaymentDetails;
	}
}
