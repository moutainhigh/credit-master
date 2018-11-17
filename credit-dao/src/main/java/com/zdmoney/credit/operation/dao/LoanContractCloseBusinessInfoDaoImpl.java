/**
 * Copyright (c) 2017, lings@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年6月2日下午5:36:34
 *
*/

package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.operation.dao.pub.ILoanContractCloseBusinessInfoDao;
import com.zdmoney.credit.operation.domain.LoanContractCloseBusinessInfo;

/**
 * ClassName:LoanContractCloseBusinessInfoDaoImpl <br/>
 * Date:     2017年6月2日 下午5:36:34 <br/>
 * @author   lings@yuminsoft.com
 */
@Repository
public class LoanContractCloseBusinessInfoDaoImpl extends BaseDaoImpl<LoanContractCloseBusinessInfo> implements
		ILoanContractCloseBusinessInfoDao {

	
	
	@Override
	public LoanContractCloseBusinessInfo selectIsFoOrgType(Long employeeId) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".selectIsFoOrgType",employeeId);
	}

	@Override
	public List<Map<String, Object>> selectShutShop() {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".selectShutShop");
	}
	
	@Override
	public List<Map<String, Object>> selectShutedShop() {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".selectShutedShop");
	}

	@Override
	public int flushShutedShop() {
		return getSqlSession().update(getIbatisMapperNameSpace()+".flushShutedShop");
	}
}
