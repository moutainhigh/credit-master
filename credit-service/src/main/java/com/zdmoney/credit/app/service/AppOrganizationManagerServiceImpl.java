package com.zdmoney.credit.app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.app.dao.pub.IAppManagerOrganizationDao;
import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.app.service.pub.IAppOrganizationManagerService;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
@Service
public class AppOrganizationManagerServiceImpl implements IAppOrganizationManagerService{

	@Autowired IAppManagerOrganizationDao appManagerOrganizationDao;
	@Override
	public Pager findAppOrganizationManagerListWithPg(Map<String, Object> paramMap) {
		return appManagerOrganizationDao.findAppManagerOrganizationListWithPg(paramMap);
	}
	
	@Override
	public void saveOrUpdateAppOrganizationManager(
			AppOrganizationManager appOrganizationManager) {
		Long id = appOrganizationManager.getId();
		if(Strings.isEmpty(id)){
			appManagerOrganizationDao.insert(appOrganizationManager);
		}else{
			appManagerOrganizationDao.updateByPrimaryKeySelective(appOrganizationManager);
		}
		
	}
	
	public  AppOrganizationManager selectAppOrganizationManagerByOrgId(Long id){
		return appManagerOrganizationDao.selectAppOrganizationManagerByOrgId(id);
	}
}
