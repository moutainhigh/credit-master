package com.zdmoney.credit.system.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductTermDao;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
@Repository
public class ProdCreditProductTermDaoImpl extends BaseDaoImpl<ProdCreditProductTerm> implements IProdCreditProductTermDao {
@Override
public void deleteByInfoIdAndTerm(ProdCreditProductTerm term) {
	getSqlSession().delete(getIbatisMapperNameSpace()+".deleteByInfoIdAndTerm", term);
	
}

@Override
public ProdCreditProductTerm findBymap(Long term, String productCd,
		String contractSource) {
	Map<String,Object> paramMap = new HashMap<String,Object>();
	paramMap.put("term", term);
	paramMap.put("productCd", productCd);
	paramMap.put("contractSource", contractSource);
	ProdCreditProductTerm prodCreditProductTerm = getSqlSession().selectOne(getIbatisMapperNameSpace() + 
			".findByTermAndProductCdAndContractSource", paramMap);
	return prodCreditProductTerm;
}
}
