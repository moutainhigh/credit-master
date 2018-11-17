package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;


public interface IProdCreditProductTermDao extends IBaseDao<ProdCreditProductTerm>{

	void deleteByInfoIdAndTerm(ProdCreditProductTerm term);
	
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public ProdCreditProductTerm findBymap(Long term,String productCd,String contractSource);
   
}