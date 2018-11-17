package com.zdmoney.credit.offer.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;

/**
 * 报盘银行字典表dao
 * @author 00232949
 *
 */
public interface IOfferBankDicDao  extends IBaseDao<OfferBankDic>{
	
	/**
	 * 根据code查询银行信息
	 * @param code
	 * @return
	 */
	public OfferBankDic findByCode(String code);
}
