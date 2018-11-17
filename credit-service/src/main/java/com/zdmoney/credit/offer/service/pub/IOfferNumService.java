package com.zdmoney.credit.offer.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.domain.OfferNum;

public interface IOfferNumService {

	Pager findWithPgByMap(Map<String, Object> paramMap);

	OfferNum findOfferNumByCode(Map<String, Object> paramMap);

	void insert(OfferNum offerNum);

	void update(OfferNum offerNum);

	void delete(OfferNum offerNum);

}
