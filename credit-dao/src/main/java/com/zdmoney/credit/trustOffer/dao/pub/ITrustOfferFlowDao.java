package com.zdmoney.credit.trustOffer.dao.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.trustOffer.domain.TrustOfferFlow;
import com.zdmoney.credit.trustOffer.vo.SubjectAmount;
import com.zdmoney.credit.trustOffer.vo.TemporaryAmountVo;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;

/**
 * Created by ym10094 on 2016/10/17.
 */
public interface ITrustOfferFlowDao extends IBaseDao<TrustOfferFlow> {
    /**
     * 获取某一期的分账金额
     * @param loanId
     * @param currentTerm
     * @return
     */
    public BigDecimal getCurrentTermSplitAccountingAmount(Long loanId,Long currentTerm);

    /**
     * 根据期数跟loan_id 获取 分账流水记录
     * @param loanId
     * @param currentTerm
     * @return
     */
    public List<TrustOfferFlow> getSplitAccountingsByLoanId(Long loanId,Long currentTerm);

    /**
     * 根据loan_id 和交易类型获取 分账流水
     * @param loanId
     * @param tradeTypes
     * @return
     */
    public List<TrustOfferFlow> getSplitAccountingsByTradeType(Long loanId,String [] tradeTypes);

    /**
     * 查询莫一天 一次性提交结清的分账流水
     * @param fundsSources
     * @param date
     * @return
     */
    public List<TrustOfferFlowVo> queryOneTimeSettlementAccountingFlow(String[] fundsSources,Date date);
    
    /**
     * 回款确认书分页
     * @param params
     * @return
     */
    public Pager queryRefundedOfMoneyConfirmationBookPage(Map<String,Object> params);

    /**
     * 根据合同来源获取分账流水
     * @param paramMap
     * @return
     */
    public List<TrustOfferFlow> getSplitAccountingsByFundSources(Map<String,Object> paramMap);

    /**
     * 获取已经分账的最大期数
     * @param loanId
     * @return
     */
    public Long getAlreadySplitAccountingMaxTerm(Long loanId);

    /**
     * 根据科目号获取分账金额
     * @param loanId
     * @param accttiles
     * @return
     */
    public BigDecimal getSplitAccountingAmountByAccttitle(Long loanId,String [] accttiles, Long currentTerm);

    /***
     * 根据交易类型获取重第N期到H期的分账金额
     * @param loanId
     * @param tradetypes
     * @param beginTerm
     * @param endTerm
     * @return
     */
    public BigDecimal getSplitAccountingAmountByTradetype(Long loanId,String [] tradetypes,Long beginTerm,Long endTerm);

    /**
     * 查询其他暂收款报表信息
     * @param params
     * @return
     */
	public List<TemporaryAmountVo> queryTemporaryAmount(Map<String, Object> params);
    /**
     * 查询对公其他暂收款报表信息
     * @param params
     * @return
     */
    public List<TemporaryAmountVo> queryPublicTemporaryAmount(Map<String, Object> params);

    /***
     *客户资金充值总额
     * @param params
     * @return
     */
    public BigDecimal queryRechargeTotalAmount(Map<String,Object> params);

    /**
     * 对公客户资金充值总额
     * @param params
     * @return
     */
    public BigDecimal queryPublicRechargeTotalAmount(Map<String,Object> params);

    /**
     * 信托专户 分账科目总金额（实时分账）
     * @param params
     * @return
     */
    public SubjectAmount queryTrustSubjectAmount(Map<String,Object> params);

    /**
     * 对公 信托专户 分账科目总金额（实时分账）
     * @param params
     * @return
     */
    public SubjectAmount queryPublicTrustSubjectAmount(Map<String,Object> params);
    /**
     * 还款明细 分账科目总金额（所有分账金额）
     * @param params
     * @return
     */
    public SubjectAmount queryRepaymentSubjectAmount(Map<String,Object> params);
    /**
     * 对公 还款明细 分账科目总金额（所有分账金额）
     * @param params
     * @return
     */
    public SubjectAmount queryPulicRepaymentSubjectAmount(Map<String,Object> params);

	/**
	 * 查询外贸3 已分账的债权与期数 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findDebitTermInfo4Wm3(Map<String, Object> params);

	/**
	 * 查询外贸3  分账信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findDebitFlowInfo4Wm3(Map<String, Object> params);
	
    public BigDecimal queryRepayFlowReportActualRepayTotalAmount(Map<String,Object> params);
}
