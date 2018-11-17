package com.zdmoney.credit.debit.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
@Repository
public class DebitBaseInfoDaoImpl extends BaseDaoImpl<DebitBaseInfo> implements IDebitBaseInfoDao {

	@Override
	public List<DebitBaseInfo> queryTodayNeedSendOfferDebitList(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findListByMap", params);
	}

	@Override
	public Integer getOfferCountForYet3Deduct(DebitBaseInfo debitBaseInfo) {
		Integer count=getSqlSession().selectOne(getIbatisMapperNameSpace()+".getOfferCountForYet3Deduct", debitBaseInfo);
		return count;
	}
	
	@Override
	public List<DebitBaseInfo> findOfferByOfferDateAndStates(Long loanId,
			Date currDate, String[] states) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("offerDate", currDate);
		paramMap.put("type", OfferTypeEnum.自动划扣.getValue());
		paramMap.put("offerInfoStates", states);
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findListByMap", paramMap);
	}
}
