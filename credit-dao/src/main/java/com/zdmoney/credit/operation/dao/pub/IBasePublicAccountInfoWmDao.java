package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfoWm;

public interface IBasePublicAccountInfoWmDao extends
		IBaseDao<BasePublicAccountInfoWm> {
	 public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePublicAccountInfoWm basePublicAccountInfoWm);
	 
	 public int updateAccountInfoForExport(Map<String, Object> params);
	 
	 public int updateAccountInfoForCancel(BasePublicAccountInfoWm basePrivateAccountInfo);


	public List<Map<String,Object>> findQueryResultMapList(BasePublicAccountInfoWm basePrivateAccountInfo);
}
