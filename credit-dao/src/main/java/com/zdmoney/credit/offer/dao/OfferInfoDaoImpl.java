package com.zdmoney.credit.offer.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.domain.OfferInfo;

/**
 * 
 * @author 00232949
 *
 */
@Repository
public class OfferInfoDaoImpl extends BaseDaoImpl<OfferInfo> implements IOfferInfoDao{

	@Override
	public int updateByPrimaryKeySelective(OfferInfo offerInfo) {
		int affectNum = 0;
		offerInfo.setUpdator("Admin");
		offerInfo.setUpdateTime(new Date());
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateByPrimaryKeySelective", offerInfo);
		return affectNum;
	}

	
	public Integer getOfferCountForNoDecuct(OfferInfo offerInfo){
		Integer count=getSqlSession().selectOne(getIbatisMapperNameSpace()+".getOfferCountForNoDecuct", offerInfo);
		return count;
	}

	@Override
	public Integer getOfferCountForYet3Deduct(OfferInfo offerInfo) {
		Integer count=getSqlSession().selectOne(getIbatisMapperNameSpace()+".getOfferCountForYet3Deduct", offerInfo);
		return count;
	}


	@Override
	public Pager getOfferInfoWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryListWithPGByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryCountWithPGByMap");
		return doPager(pager,paramMap);
	}

	public List<OfferInfo> findOfferReturnList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findOfferReturnList", params);
	}
	
	public  int updateOfferInfo(Map<String, Object> paramMap){
		int affectNum = 0;
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateOfferInfo", paramMap);
		return affectNum;
	}

	public List<OfferInfo> queryTodayNeedSendOfferList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".queryTodayNeedSendOfferList", params);
	}

	public String queryDebitTypeByLoanId(Long loanId) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".queryDebitTypeByLoanId", loanId);
	}

	public List<Map<String, Object>> queryOffLineOfferInfo(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".queryOffLineOfferInfo", params);
	}


	@Override
	public String getoffercount(String usercode) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".getoffercountByusercode", usercode);
	}
}
