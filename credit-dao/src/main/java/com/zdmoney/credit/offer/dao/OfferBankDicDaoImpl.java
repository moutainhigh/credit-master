package com.zdmoney.credit.offer.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;

@Repository
public class OfferBankDicDaoImpl extends BaseDaoImpl<OfferBankDic> implements IOfferBankDicDao{
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYCODE = ".findByCode";
	
	
	/**
	 * 根据code查询银行信息
	 * @param code
	 * @return
	 */
	@Override
	public OfferBankDic findByCode(String code) {
		OfferBankDic bank = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYCODE, code);
		return bank;
	}

}
