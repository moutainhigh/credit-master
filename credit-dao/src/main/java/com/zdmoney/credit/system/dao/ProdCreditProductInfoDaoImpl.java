package com.zdmoney.credit.system.dao;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductInfoDao;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
@Repository
public class ProdCreditProductInfoDaoImpl extends BaseDaoImpl<ProdCreditProductInfo> implements IProdCreditProductInfoDao{

	/**
     * 通过loanType查找
     * @param loanType
     * @return
     */
	@Override
	public ProdCreditProductInfo findByLoanType(String loanType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanType", loanType);
		
		ProdCreditProductInfo creditProduct = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByLoanType", paramMap);
		
		return creditProduct;
	}

	@Override
	public ProdCreditProductInfo findById(ProdCreditProductTerm prodCreditProductTerm) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findById",prodCreditProductTerm);
	}
	
	@Override
	public ProdCreditProductInfo getIsThere(
			ProdCreditProductInfo prodCreditProductInfo) {
		// TODO Auto-generated method stub
		return  getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getIsThere",prodCreditProductInfo);
	}
}
