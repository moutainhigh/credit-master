package com.zdmoney.credit.offer.dao;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.repay.vo.RepayStateDetail;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OfferFlowDaoImpl extends BaseDaoImpl<OfferFlow> implements IOfferFlowDao{

	@Override
	public BigDecimal getPaidAmountOnRepayDay(Map<String, Object> param) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getPaidAmountOnRepayDay", param);
	}

	@Override
	public BigDecimal getPaidAmountBeforeEndDay(Map<String, Object> param) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getPaidAmountBeforeEndDay", param);
	}

	/**
	 * 根据tradeNo
	 * @param tradeNo
	 * @return
	 */
	@Override
	public List<OfferFlow> findByTradeNo(String tradeNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tradeNo", tradeNo);
		List<OfferFlow> offerFlows = findListByMap(paramMap);
		return offerFlows;
	}

	/**
	 * 计算期末余额
	 * @param loanId
	 * @param tradeNo
	 * @return
	 */
	@Override
	public BigDecimal findQimoBalance(Long loanId, String tradeNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("tradeNo", tradeNo);
		paramMap.put("acctTitle", "111");
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findQimoBalance", paramMap);
	}

	/**
	 * 获取开户总金额
	 * @param param
	 * @return
	 */
	@Override
	public BigDecimal getTradeAmount(Map<String, Object> param) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findTradeAmountByAppoAcct", param);
	}

	/**
	 * 助学贷机构总收入
	 * @param param
	 * @return
	 */
	@Override
	public BigDecimal getZhuxueOrganizationTotalIncome(Map<String, Object> param) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findZhuxueOrganizationTotalIncome", param);
	}

	/**
	 * 助学贷机构总支出
	 * @param param
	 * @return
	 */
	@Override
	public BigDecimal getZhuxueOrganizationTotalPay(Map<String, Object> param) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findZhuxueOrganizationTotalPay", param);
	}

	@Override
	public List<OfferFlow> findByTradeNoaccTitleAsc(String tradeNo,String[] acctTitles) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tradeNo", tradeNo);
		paramMap.put("acctTitles",acctTitles);
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByTradeNoaccTitleAsc", paramMap);
	}

	@Override
	public List<OfferFlow> findRecordAccountsFlowsBytradeNo(String tradeNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tradeNo", tradeNo);
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findRecordAccountsFlowsBytradeNo", paramMap);
	}

	@Override
	public List<OfferFlow> findRecordAccountsFlows(Long loanId, Date tradeDate) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("loanId",loanId);
		paramMap.put("tradeDate",tradeDate);
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findRecordAccountsFlows", paramMap);
	}

	@Override
	public List<OfferFlow> findCancelAccountsFlowsBytradeNo(String tradeNo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tradeNo", tradeNo);
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findCancelAccountsFlowsBytradeNo", paramMap);
	}

	@Override
	public BigDecimal getMoreTermFineAmount(Long loanId, List<Long> terms) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("terms",terms);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getMoreTermFineAmount", paramMap);
	}

	@Override
	public List<OfferFlow> findOverDueOfferFlow4Lufax(Map<String, Object> map) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOverDueOfferFlow4Lufax", map);
	}

	@Override
	public BigDecimal getCurrenTermAlreadyRepayTotalMoney(Long loanId, String currentTerm) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("currentTerm",currentTerm);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getCurrenTermAlreadyRepayTotalMoney", paramMap);
	}

	@Override
	public int getHistoryReleifTime(Long loanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getHistoryReleifTime", paramMap);
	}

	@Override
	public BigDecimal getHistoryReliefAmount(Long loanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getHistoryReliefAmount", paramMap);
	}

	@Override
	public BigDecimal getAlreadyPayFineAmount(Long loanId, String term) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("currentTerm",term);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getAlreadyPayFineAmount", paramMap);	}

	@Override
	public RepayStateDetail getRepayStateDetailRealValue(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getRepayStateDetailRealValue", paramMap);
	}

}
