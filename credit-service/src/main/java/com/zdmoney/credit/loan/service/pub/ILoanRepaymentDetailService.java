package com.zdmoney.credit.loan.service.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;


/**
 * 
 * @author 00232949
 *
 */
public interface ILoanRepaymentDetailService {

	
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
	 * 更新数据
	 * @param repaymentDetail
	 */
	public int update(LoanRepaymentDetail repaymentDetail);
	
	/**
	 * afterLoanSetvice中drawJimuRisk方法中，查询SumDeficit方法
	 * @param map 包含loanId、currDate、minDate、repaymentState参数
	 * @return
	 */
	public BigDecimal getDrawJimuRiskSumDeficit(Map<String, Object> map);
	/**
	 * afterLoanSetvice中drawRiskFee方法中，查询SumDeficit方法
	 * @param repaymentMap
	 * @return
	 */
	public BigDecimal getDrawRiskSumDeficit(Map<String, Object> repaymentMap);

	/**
	 * 立即更新，独立事物
	 * @param para
	 */
	public void updateNow(LoanRepaymentDetail repaymentDetail);

	/**
	 * 一次性结清时，更新所有未还的还款计划为结清
	 * @param id
	 * @param tradeDate
	 */
	public void updateYCXJQAllStateToSettlementByLoanId(Long id, Date tradeDate);

	/**
	 * 一次性结清时，还款计划表剩余金额清零
	 * @param id
	 */
	public void updateYCXJQAllDeficitToZeroByLoanId(Long id);

	/**
	 * 得到指定还款日期的还款计划
	 * @param loanInfo
	 * @param lastRepayDate
	 * @return
	 */
	public LoanRepaymentDetail findByLoanAndReturnDate(VLoanInfo loanInfo,
			Date lastRepayDate);

	public LoanRepaymentDetail findRepaymentDetailByLoanAndReturnDate(Map<String, String> map);

	/**
	 * 查询还款等级
	 * @param map
	 * @return
	 */
	public String findRepaymentLevel(Map<String, Object> map);
	
	/**
	 * 处理 还款计划上传给外贸3的结果
	 */
	public void executeRepayPlanUpload();
	
	/**
	 * 根据map参数，查询还款计划
	 * @param map
	 * @return
	 */
	public List<LoanRepaymentDetail> findListByMap(Map<String, Object> map);
	
	/**
	 * 根据loanId得到还款计划list
	 */
	public List<LoanRepaymentDetail> findLoanRepaymentDetailListByLoanId(Long loanId);
}
