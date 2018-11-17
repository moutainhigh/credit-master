package com.zdmoney.credit.app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.app.dao.pub.IAppEmployeeManagerDao;
import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.app.service.pub.IAppEmployeeManagerService;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;

@Service
public class AppEmployeeManagerServiceImpl implements
		IAppEmployeeManagerService {
	
	@Autowired IAppEmployeeManagerDao appEmployeeManagerDao;
	@Override
	public void saveAppEmployeeManager(AppEmployeeManager appEmployeeManager) {
		Long id = appEmployeeManager.getId();
		if(Strings.isEmpty(id)){
			appEmployeeManagerDao.insert(appEmployeeManager);
		}else{
			appEmployeeManagerDao.updateByPrimaryKeySelective(appEmployeeManager);
		}
	}

	@Override
	public Pager findAppEmployeeManagerListWithPg(Map<String, Object> paramMap) {
		return appEmployeeManagerDao.findAppEmployeeManagerListWithPg(paramMap);
	}
	
	public AppEmployeeManager selectAppEmployeeManagerByEmployeeId(Long employeeId){
		return appEmployeeManagerDao.selectAppEmployeeManagerByEmployeeId(employeeId);
	}

}
