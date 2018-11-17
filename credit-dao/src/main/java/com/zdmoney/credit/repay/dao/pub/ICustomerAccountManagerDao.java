package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.VCustomerAccountManager;

public interface ICustomerAccountManagerDao extends IBaseDao<VCustomerAccountManager>{
	
	/**
	 * 查询客户账户信息列表
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findCustomerAccountInfoList(Map<String,Object> params);
	
	/**
	 * 查询客户账户信息列表数量
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public int findCustomerAccountInfoCount(Map<String,Object> params);
	
	/**
	 * 查询客户账户信息
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findCustomerAccountInfo(Map<String,Object> params);
}
