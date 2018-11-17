package com.zdmoney.credit.offer.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferNumDao;
import com.zdmoney.credit.offer.domain.OfferNum;

@Repository
public class OfferNumDaoImpl extends BaseDaoImpl<OfferNum> implements IOfferNumDao {

	@Override
	public OfferNum findOfferNumByCode(Map<String, Object> paramMap) {
		  
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findOfferNumByCode",paramMap);
	}

	@Override
	public void delete(OfferNum offerNum) {
		 
		getSqlSession().delete(getIbatisMapperNameSpace() + ".delete",offerNum);
	}

}
