package com.zdmoney.credit.offer.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.dao.pub.IOfferNumDao;
import com.zdmoney.credit.offer.domain.OfferNum;
import com.zdmoney.credit.offer.service.pub.IOfferNumService;

/**
 * 划扣次数Service
 * @author guocl
 */
@Service
public class OfferNumServiceImpl implements IOfferNumService{

	@Autowired
	IOfferNumDao offerNumDao;
	
	/**
	 * 根据Map去查找
	 * @param param
	 * @return
	 */
	@Override
	public Pager findWithPgByMap(Map<String, Object> param) {
		Pager pager = offerNumDao.findWithPgByMap(param);
		return pager;
	}

	@Override
	public OfferNum findOfferNumByCode(Map<String, Object> paramMap) {
		OfferNum offerNum = offerNumDao.findOfferNumByCode(paramMap);
		return offerNum;
	}

	@Override
	public void insert(OfferNum offerNum) {
		offerNumDao.insert(offerNum);
		
	}

	@Override
	public void update(OfferNum offerNum) {
		offerNumDao.update(offerNum);
		
	}

	@Override
	public void delete(OfferNum offerNum) {
		offerNumDao.delete(offerNum);
	}
	
}
