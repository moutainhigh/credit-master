package com.zdmoney.credit.offer.dao.pub;

import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferNum;

public interface IOfferNumDao extends IBaseDao<OfferNum>{

	OfferNum findOfferNumByCode(Map<String, Object> paramMap);

	void delete(OfferNum offerNum);
	
}
