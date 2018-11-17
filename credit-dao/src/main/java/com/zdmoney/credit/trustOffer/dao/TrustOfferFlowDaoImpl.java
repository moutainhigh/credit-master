package com.zdmoney.credit.trustOffer.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.trustOffer.dao.pub.ITrustOfferFlowDao;
import com.zdmoney.credit.trustOffer.domain.TrustOfferFlow;
import com.zdmoney.credit.trustOffer.vo.SubjectAmount;
import com.zdmoney.credit.trustOffer.vo.TemporaryAmountVo;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;

/**
 * Created by ym10094 on 2016/10/17.
 */
@Repository
public class TrustOfferFlowDaoImpl  extends BaseDaoImpl<TrustOfferFlow> implements ITrustOfferFlowDao {

    @Override
    public BigDecimal getCurrentTermSplitAccountingAmount(Long loanId, Long currentTerm) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("currentTerm",currentTerm);
        BigDecimal sumAmount = this.getSqlSession().selectOne(getIbatisMapperNameSpace()+".getSplitAccountingAmount",paramMap);
        return sumAmount;
    }

    @Override
    public List<TrustOfferFlow> getSplitAccountingsByLoanId(Long loanId, Long currentTerm) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("currentTerm",currentTerm);
//        List<TrustOfferFlow> trustOfferFlows = this.getSqlSession().selectList(getIbatisMapperNameSpace()+".getSplitAccountingsByLoanId",paramMap);
        List<TrustOfferFlow> trustOfferFlows = this.findListByMap(paramMap);
        return trustOfferFlows;
    }

    @Override
    public List<TrustOfferFlow> getSplitAccountingsByTradeType(Long loanId, String[] tradeTypes) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("tradeTypes",tradeTypes);
        List<TrustOfferFlow> trustOfferFlows = this.findListByMap(paramMap);
        return trustOfferFlows;
    }

    @Override
    public List<TrustOfferFlowVo> queryOneTimeSettlementAccountingFlow(String[] fundsSources, Date date) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("fundsSources",fundsSources);
        paramMap.put("date",date);
        return getSqlSession().selectList(getIbatisMapperNameSpace()+".queryOneTimeSettlementAccountingFlow",paramMap);
    }
    
    public Pager queryRefundedOfMoneyConfirmationBookPage(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryRefundedOfMoneyConfirmationtInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryRefundedOfMoneyConfirmationInfosCount");
        return doPager(pager,params);
    }

    @Override
    public List<TrustOfferFlow> getSplitAccountingsByFundSources(Map<String,Object> paramMap) {
        List<TrustOfferFlow> trustOfferFlows =  getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSplitAccountingsByFundSources",paramMap);
        return trustOfferFlows;
    }

    @Override
    public Long getAlreadySplitAccountingMaxTerm(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        Long term = this.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getAlreadySplitAccountingMaxTerm",paramMap);
        return term;
    }

    @Override
    public BigDecimal getSplitAccountingAmountByAccttitle(Long loanId, String[] accttiles ,Long currentTerm) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("currentTerm",currentTerm);
        paramMap.put("acctTitles",accttiles);
        BigDecimal sumAmount = this.getSqlSession().selectOne(getIbatisMapperNameSpace()+".getSplitAccountingAmount",paramMap);
        return sumAmount;
    }

    @Override
    public BigDecimal getSplitAccountingAmountByTradetype(Long loanId, String[] tradetypes, Long beginTerm, Long endTerm) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("beginTerm",beginTerm);
        paramMap.put("endTerm",endTerm);
        paramMap.put("tradetypes",tradetypes);
        BigDecimal sumAmount = this.getSqlSession().selectOne(getIbatisMapperNameSpace()+".getSplitAccountingAmount",paramMap);
        return sumAmount;
    }


    /**
     * 查询其他暂收款报表信息
     */
	@Override
	public List<TemporaryAmountVo> queryTemporaryAmount(Map<String, Object> params) {
		List<TemporaryAmountVo> temporaryAmountList =  getSqlSession().selectList(getIbatisMapperNameSpace() 
				+ ".queryTemporaryAmount",params);
		
        return temporaryAmountList;
	}

    @Override
    public List<TemporaryAmountVo> queryPublicTemporaryAmount(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryPublicTemporaryAmount",params);
    }

    @Override
    public BigDecimal queryRechargeTotalAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryRechargeTotalAmount",params);
    }

    @Override
    public BigDecimal queryPublicRechargeTotalAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryPublicRechargeTotalAmount",params);
    }

    @Override
    public SubjectAmount queryTrustSubjectAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryTrustSubjectAmount",params);
    }

    @Override
    public SubjectAmount queryPublicTrustSubjectAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryPublicTrustSubjectAmount",params);
    }

    @Override
    public SubjectAmount queryRepaymentSubjectAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryRepaymentSubjectAmount",params);
    }

    @Override
    public SubjectAmount queryPulicRepaymentSubjectAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryPulicRepaymentSubjectAmount",params);
    }

    @Override
	public List<Map<String, Object>> findDebitTermInfo4Wm3(Map<String, Object> params) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDebitTermInfo4Wm3",params);
	}

	@Override
	public List<Map<String, Object>> findDebitFlowInfo4Wm3(
			Map<String, Object> params) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDebitFlowInfo4Wm3",params);
	}
	
    @Override
    public BigDecimal queryRepayFlowReportActualRepayTotalAmount(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryRepayFlowReportActualRepayTotalAmount",params);
    }
}
