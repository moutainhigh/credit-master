package com.zdmoney.credit.offer.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankHeadDao;
import com.zdmoney.credit.offer.domain.OfferYongyoubankHead;

@Repository
public class OfferYongyoubankHeadDaoImpl extends BaseDaoImpl<OfferYongyoubankHead>implements IOfferYongyoubankHeadDao {

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYCODE = ".findByCode";
	
	
	/**
	 * 通过code查找总行信息
	 * @param code
	 * @return
	 */
	@Override
	public OfferYongyoubankHead findByCode(String code) {
		OfferYongyoubankHead head = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYCODE, code);
		return head;
	}


}
