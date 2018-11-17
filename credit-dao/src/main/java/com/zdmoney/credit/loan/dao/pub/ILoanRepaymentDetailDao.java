package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ILoanRepaymentDetailDao  extends IBaseDao<LoanRepaymentDetail>{

	/**
	 * 根据债权基本表ID和还款状态以及时间来查询还款计划
	 * @param map
	 * @return
	 */
	public List<LoanRepaymentDetail> findByLoanIdAndRepaymentState(Map<String, Object> map);
	

	/**
	 * 根据债权基本表ID和还款状态查询剩余欠款之和
	 * @param map
	 * @return
	 */
	public BigDecimal findByLoanIdAndNotRepaymentStateInSum(Map<String, Object> map);
	
	/**
	 * afterLoanSetvice中drawJimuRisk方法中，查询SumDeficit方法
	 * @param map 包含loanId、currDate、minDate、repaymentState参数
	 * @return
	 */
	public BigDecimal getDrawJimuRiskSumDeficit(Map<String, Object> map);
	/**
	 *  afterLoanSetvice中drawRiskFee方法中，查询SumDeficit方法
	 * @param repaymentMap
	 * @return
	 */
	public BigDecimal getDrawRiskSumDeficit(Map<String, Object> repaymentMap);
	
	/**
	 * 一次性结清时，更新所有未还的还款计划为结清
	 * @param loanid
	 * @param tradeDate
	 * @return
	 */
	public int updateYCXJQAllStateToSettlementByLoanId(Long loanid, Date tradeDate);


	/**
	 * 一次性结清时，还款计划表剩余金额清零
	 * @param id
	 * @return
	 */
	public int updateYCXJQAllDeficitToZeroByLoanId(Long id);
	
	/**
	 * 根据loanId查询还款计划
	 * @param loanId
	 * @return
	 */
	public List<LoanRepaymentDetail> findByLoanId(Long loanId);
	
	/**
	 * 根据loanId删除还款计划
	 * @param loanId
	 */
	public void deleteByLoanId(Long loanId);


	/**
	 * 分页查询还款计划
	 * @param paramMap 参数集合
	 * @return
	 */
	public Pager getRepaymentDatailWithPg(Map<String,Object> paramMap);
	
	/**
	 * 根据loanId和currentTerm查询
	 * @param loanId
	 * @param currentTerm
	 * @return
	 */
	public LoanRepaymentDetail findByLoanIdAndCurrentTerm(Long loanId, Long currentTerm);
	
	/**
	 * 根据loanId查询当前利息
	 * @param loanId
	 * @return
	 */
	public BigDecimal findCurrentAccrual(Long loanId, Long time);
	
	public LoanRepaymentDetail findRepaymentDetailByLoanAndReturnDate(Map<String, String> map); 
	
	/**
	 * 查询还款等级
	 * @param map
	 * @return
	 */
	public String findRepaymentLevel(Map<String, Object> map);

	/**
	 * 根据loanId 跟 currentTerm查询等于当前期数的还款记录
	 * @param loanId
	 * @param currentTerms
	 * @return
	 */
	public List<LoanRepaymentDetail> findEqualCurrentTerm(Long loanId,List<Long> currentTerms);

	/**
	 * 获取信托分账需要的还款计划 根据实际 loanID 还款状态
	 * @param paramMap
	 * @return
	 */
	public List<LoanRepaymentDetail> findTrustRepaymentState(Map<String,Object> paramMap);
	/**
	 * 根据loanId 跟 currentTerm查询大于当前期数的还款记录
	 * @param loanId
	 * @param currentTerm
	 * @return
	 */
	public List<LoanRepaymentDetail> findBigCurrentTerm(Long loanId,Long currentTerm);

	/**
	 * 根据来源查询出当期正常还款和提前还款的明细
	 * @param loanBelong
	 * @param returnDate
	 * @return
	 */
	public List<LoanRepaymentDetail> findByLoanBelongAndReturnDate(
			String loanBelong, Date returnDate);
	
	/**
	 * 提前结清 
	 * @param map
	 * @return
	 */
	public List<LoanRepaymentDetail> findOverdueCompensatoryToLufax(Map<String, Object> map);

	/**
	 * 计算实际还款日换了多少期
	 * @param loanId
	 * @param factReturnDate
	 * @return
	 */
	public List<LoanRepaymentDetail> findByLoanIdAndFactReturnDate(Long loanId,
			Date factReturnDate);
}
