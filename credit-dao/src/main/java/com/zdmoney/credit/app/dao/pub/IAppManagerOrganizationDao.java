package com.zdmoney.credit.app.dao.pub;

import java.util.Map;

import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IAppManagerOrganizationDao extends IBaseDao<AppOrganizationManager> {
	
	public Pager findAppManagerOrganizationListWithPg(Map<String, Object> paramMap);
	
	public  AppOrganizationManager selectAppOrganizationManagerByOrgId(Long id);
	
}
