package com.zdmoney.credit.app.dao.pub;

import java.util.Map;

import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IAppEmployeeManagerDao extends IBaseDao<AppEmployeeManager> {
	
	public Pager findAppEmployeeManagerListWithPg(Map<String, Object> paramMap);
	
	public AppEmployeeManager selectAppEmployeeManagerByEmployeeId(Long id);
	
}
