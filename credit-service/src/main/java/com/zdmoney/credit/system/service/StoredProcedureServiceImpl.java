package com.zdmoney.credit.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.system.dao.pub.IStoredProcedureDao;
import com.zdmoney.credit.system.service.pub.IStoredProcedureService;

@Service
public class StoredProcedureServiceImpl implements IStoredProcedureService{
	
	@Autowired
	private IStoredProcedureDao storedProcedureDao;

	@Override
	public int rollbackGrantloan(Long loanId, String userCode) {
		return storedProcedureDao.rollbackGrantloan(loanId, userCode);
	}
	
	
}
