package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

public interface IProdCreditProductInfoDao extends
		IBaseDao<ProdCreditProductInfo> {
	/**
	 * 通过loanType查找
	 * 
	 * @param loanType
	 * @return
	 */
	public ProdCreditProductInfo findByLoanType(String loanType);

	public ProdCreditProductInfo findById(
			ProdCreditProductTerm prodCreditProductTerm);

	public ProdCreditProductInfo getIsThere(
			ProdCreditProductInfo prodCreditProductInfo);
}