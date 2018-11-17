package com.zdmoney.credit.ljs.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.ljs.dao.pub.IPublicVirtualAccountDao;
import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;

@Repository
public class PublicVirtualAccountDaoImpl extends BaseDaoImpl<PublicVirtualAccount> implements IPublicVirtualAccountDao {

	@Override
	public int addAmtByAccountType(String accountType, BigDecimal addAmt) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountType", accountType);
		map.put("addAmt", addAmt);
		int affectNum = 0;		
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".addAmtByAccountType", map);
        return affectNum;
	}

	@Override
	public PublicVirtualAccount findByAccountType(String accountType) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByAccountType", accountType);
	}

	@Override
	public int subAmtByAccountType(String accountType, BigDecimal subAmt) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountType", accountType);
		map.put("addAmt", subAmt);
		int affectNum = 0;		
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".subAmtByAccountType", map);
        return affectNum;
	}
}
