package com.zdmoney.credit.app.service.pub;

import java.util.Map;

import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.common.util.Pager;

public interface IAppOrganizationManagerService {
	public Pager findAppOrganizationManagerListWithPg(Map<String, Object> paramMap);
	
	public void saveOrUpdateAppOrganizationManager(AppOrganizationManager appOrganizationManager);
	
	public  AppOrganizationManager selectAppOrganizationManagerByOrgId(Long id);
}
