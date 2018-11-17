package com.zdmoney.credit.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.system.dao.pub.ISysConflictPreventionDao;
import com.zdmoney.credit.system.domain.SysConflictPrevention;
import com.zdmoney.credit.system.service.pub.ISysConflictPreventionService;

@Service
@Transactional
public class SysConflictPreventionServiceImpl implements ISysConflictPreventionService {
	
	@Autowired
	ISysConflictPreventionDao sysConflictPreventionDao;

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveNow(String key) {
		
		SysConflictPrevention sysConflictPrevention = new SysConflictPrevention();
		sysConflictPrevention.setKey(key);
		sysConflictPreventionDao.insert(sysConflictPrevention);
		
	}
	
	
}
