package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.operation.dao.pub.IBasePublicAccountInfoWmDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfoWm;
@Repository
public class BasePublicAccountInfoWmDaoImpl extends BaseDaoImpl<BasePublicAccountInfoWm> implements
		IBasePublicAccountInfoWmDao {
	 public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePublicAccountInfoWm basePublicAccountInfoWm) {
	        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findPublicAccountReceiveWMInfo",basePublicAccountInfoWm);
	 }
	 
	 public int updateAccountInfoForExport(Map<String, Object> params) {
	        return getSqlSession().update(getIbatisMapperNameSpace() + ".updateAccountInfoWMForExport", params);
	 }

	 public int updateAccountInfoForCancel(BasePublicAccountInfoWm basePrivateAccountInfo) {
	        return getSqlSession().update(getIbatisMapperNameSpace() + ".updateAccountInfoWMForCancel", basePrivateAccountInfo);
	 }

	@Override
	public List<Map<String, Object>> findQueryResultMapList(BasePublicAccountInfoWm basePrivateAccountInfo) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findQueryResultMapList",basePrivateAccountInfo);
	}
}
