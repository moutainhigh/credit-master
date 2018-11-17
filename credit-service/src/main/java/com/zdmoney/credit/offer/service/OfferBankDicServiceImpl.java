package com.zdmoney.credit.offer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.offer.service.pub.IOfferBankDicService;

@Service
public class OfferBankDicServiceImpl implements IOfferBankDicService{
	
	@Autowired @Qualifier("offerBankDicDaoImpl")
	IOfferBankDicDao offerBankDicDaoImpl;
	
	/**
	 * 跟据实体查询结果集
	 * @param offerBankDic
	 * @return
	 */
	public List<OfferBankDic> findListByVo(OfferBankDic offerBankDic) {
		return offerBankDicDaoImpl.findListByVo(offerBankDic);
	}
	
}
