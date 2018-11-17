package com.zdmoney.credit.offer.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankBranchDao;
import com.zdmoney.credit.offer.domain.OfferYongyoubankBranch;

@Repository
public class OfferYongyoubankBranchDaoImpl extends BaseDaoImpl<OfferYongyoubankBranch>implements IOfferYongyoubankBranchDao {

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYBANKID = ".findByBankId";
	
	
	/**
	 * 根据bankId查找支行信息
	 * @param bankId
	 * @return
	 */
	@Override
	public OfferYongyoubankBranch findByBankId(String bankId) {
		OfferYongyoubankBranch branch = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYBANKID, bankId);
		return branch;
	}

}
