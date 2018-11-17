package com.zdmoney.credit.offer.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferYongyoubankHead;

public interface IOfferYongyoubankHeadDao extends IBaseDao<OfferYongyoubankHead> {

	/**
	 * 通过code查找总行信息
	 * @param code
	 * @return
	 */
	public OfferYongyoubankHead findByCode(String code);
}
