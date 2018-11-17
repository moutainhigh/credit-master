package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.ICustomerAccountManagerDao;
import com.zdmoney.credit.repay.vo.VCustomerAccountManager;

@Repository("customerAccountManagerDao")
public class CustomerAccountManagerDaoImpl extends BaseDaoImpl<VCustomerAccountManager> implements ICustomerAccountManagerDao{

	/**
	 * 查询客户账户信息列表
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findCustomerAccountInfoList(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findCustomerAccountInfoList", params);
		return result;
	}

	/**
	 * 查询客户账户信息列表数量
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public int findCustomerAccountInfoCount(Map<String, Object> params) {
		int result = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findCustomerAccountInfoCount", params);
		
		return result;
	}

	/**
	 * 查询客户账户信息
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findCustomerAccountInfo(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findCustomerAccountInfo", params);
		return result;
	}
}
