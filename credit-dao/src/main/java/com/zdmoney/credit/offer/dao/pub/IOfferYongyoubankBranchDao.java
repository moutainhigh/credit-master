package com.zdmoney.credit.offer.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferYongyoubankBranch;

public interface IOfferYongyoubankBranchDao extends IBaseDao<OfferYongyoubankBranch> {
	
	/**
	 * 根据bankId查找支行信息
	 * @param bankId
	 * @return
	 */
	public OfferYongyoubankBranch findByBankId(String bankId);
}
