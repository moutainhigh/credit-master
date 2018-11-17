package com.zdmoney.credit.offer.service.pub;

import java.util.List;

import com.zdmoney.credit.offer.domain.OfferBankDic;

public interface IOfferBankDicService {
	
	/**
	 * 跟据实体查询结果集
	 * @param offerBankDic
	 * @return
	 */
	public List<OfferBankDic> findListByVo(OfferBankDic offerBankDic);
	
}
