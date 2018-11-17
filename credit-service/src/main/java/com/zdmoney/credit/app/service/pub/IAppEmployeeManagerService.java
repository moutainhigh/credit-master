package com.zdmoney.credit.app.service.pub;

import java.util.Map;

import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.common.util.Pager;

public interface IAppEmployeeManagerService {
	public void saveAppEmployeeManager(AppEmployeeManager appEmployeeManager);
	
	public Pager findAppEmployeeManagerListWithPg(Map<String, Object> paramMap);
	
	public AppEmployeeManager selectAppEmployeeManagerByEmployeeId(Long employeeId);
	
}
