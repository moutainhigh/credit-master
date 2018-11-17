package com.zdmoney.credit.offer.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;


@Repository
public class OfferRepayInfoDaoImpl extends BaseDaoImpl<OfferRepayInfo> implements IOfferRepayInfoDao{

	@Override
	public List<OfferRepayInfo> findListByFinanceVo(FinanceVo params) {
		/*def searchClosure = {
        eq('riAccount',params.loanId)
        //not{'in'('riTradeCode',[Const.TRADE_CODE_OPENACC.toString(),Const.TRADE_CODE_OPENACC_ASC.toString(),Const.TRADE_CODE_DRAWRISK.toString(),Const.TRADE_CODE_DRAWRISK_STUDENT.toString()]) }
        not{'in'('riTradeCode',[Const.TRADE_CODE_OPENACC_ASC.toString(),Const.TRADE_CODE_DRAWRISK.toString(),Const.TRADE_CODE_DRAWRISK_STUDENT.toString()]) }//Const.TRADE_CODE_OPENACC.toString()去掉了（2015-07-22）
        order('riTradeNo','asc')
    }*/
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("loanId", params.getLoanId());
		paramMap.put("notEqTradeCode", new String[]{Const.TRADE_CODE_OPENACC
				,Const.TRADE_CODE_OPENACC_ASC,Const.TRADE_CODE_DRAWRISK,Const.TRADE_CODE_DRAWRISK_STUDENT});
		paramMap.put("sort", "trade_no");
		List<OfferRepayInfo> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findListByFinanceVo", paramMap);
		
		return rstList;
	}

	@Override
	public List<OfferRepayInfo> getDrawJimuRiskRepayInfo(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getDrawRiskRepayInfo", params);
	}

	@Override
	public OfferRepayInfo getLoanlastDrawRisk(Map<String, Object> params) {
		
		List<OfferRepayInfo> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getDrawRiskRepayInfo", params);
		if(rstList!=null && rstList.size()>0){
			return rstList.get(0);
		}
		return null;
	}

	@Override
	public OfferRepayInfo getLoanLastRepayInfoByTradeCode(
			Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getLoanLastRepayInfoByTradeCode", params);
	}

	/**
	 * 分页查询还款明细数据
	 * @param paramMap 参数集合
	 * @return
	 */
	@Override
	public Pager getOfferRepayInfoWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findListWithPGByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".findCountByMap");
		return doPager(pager,paramMap);
	}

@Override
public OfferRepayInfo getLoanLastRepayInfoById(Map<String, Object> paramMap) {
	
	return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getLoanLastRepayInfoById", paramMap);
}

	@Override
	public List<OfferRepayInfo> getTrustOfferRepayInfo(Map<String, Object> paramMap) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+ ".getTrustOfferRepayInfo",paramMap);
	}

	@Override
	public OfferRepayInfo findByTradeNo(String tradeNo) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("tradeNo",tradeNo);
		return  getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByTradeNo", paramMap);
	}

	@Override
	public List<Map<String, Object>> findSubAccountDetailList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findSubAccountDetailList", params);
		
	}

	@Override
	public List<Map<String, Object>> findPublicSubAccountDetailList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findPublicSubAccountDetailList", params);
	}

	@Override
	public List<Map<String, Object>> findSubAccountDetailList4BHXT(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findSubAccountDetailList4BHXT", params);
	}
}
