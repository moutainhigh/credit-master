package com.zdmoney.credit.offer.dao.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.repay.vo.RepayStateDetail;

public interface IOfferFlowDao  extends IBaseDao<OfferFlow>{

	public BigDecimal getPaidAmountOnRepayDay(Map<String, Object> param);
	
	public BigDecimal getPaidAmountBeforeEndDay(Map<String, Object> param);
	
	/**
	 * 根据tradeNo
	 * @param tradeNo
	 * @return
	 */
	public List<OfferFlow> findByTradeNo(String tradeNo);
	
	/**
	 * 计算期末余额
	 * @param loanId
	 * @param tradeNo
	 * @return
	 */
	public BigDecimal findQimoBalance(Long loanId, String tradeNo);
	
	/**
	 * 获取开户总金额
	 * @param param
	 * @return
	 */
	public BigDecimal getTradeAmount(Map<String, Object> param);
	
	/**
	 * 助学贷机构总收入
	 * @param param
	 * @return
	 */
	public BigDecimal getZhuxueOrganizationTotalIncome(Map<String, Object> param);
	
	/**
	 * 助学贷机构总支出
	 * @param param
	 * @return
	 */
	public BigDecimal getZhuxueOrganizationTotalPay(Map<String, Object> param);
	/**
	 * 根据tradeNo 和 科目号 查询 根据ID按照升序排列
	 * @param tradeNo
	 * @return
	 */
	public List<OfferFlow> findByTradeNoaccTitleAsc(String tradeNo,String[] acctTitles);

	public List<OfferFlow> findRecordAccountsFlowsBytradeNo(String tradeNo);
	/**
	 * 查找挂账分账流水
	 * @param loanId
	 * @param tradeDate
	 * @return
	 */
	public List<OfferFlow> findRecordAccountsFlows(Long loanId,Date tradeDate);

	public List<OfferFlow> findCancelAccountsFlowsBytradeNo(String tradeNo);

	/**
	 * 获取多期的罚息
	 * @param loanId
	 * @param terms
	 * @return
	 */
	public  BigDecimal getMoreTermFineAmount(Long loanId,List<Long> terms);

	/**
	 *获取陆金所逾期还款流水
	 * @param map
	 * @return
	 */
	public List<OfferFlow> findOverDueOfferFlow4Lufax(Map<String, Object> map);

	/**
	 * 获取对应累计已还金额
	 * @param loanId
	 * @param currentTerm
	 * @return
	 */
	public BigDecimal getCurrenTermAlreadyRepayTotalMoney(Long loanId,String currentTerm);

	/**
	 * 获取历史减免次数
	 * @param loanId
	 * @return
	 */
	public int getHistoryReleifTime(Long loanId);

	/**
	 * 获取历史减免金额
	 * @param loanId
	 * @return
	 */
	public BigDecimal getHistoryReliefAmount(Long loanId);

	/**
	 * 获取某一期的已收罚息
	 * @param loanId
	 * @param term
	 * @return
	 */
	public  BigDecimal getAlreadyPayFineAmount(Long loanId,String term);
	
	
	/**
	 * 得到还款状态的实际还款的值 本金 利息 罚息 实际还款日
	 * @param paramMap
	 * @return
	 */
	public RepayStateDetail getRepayStateDetailRealValue(Map<String, Object> paramMap);
}
