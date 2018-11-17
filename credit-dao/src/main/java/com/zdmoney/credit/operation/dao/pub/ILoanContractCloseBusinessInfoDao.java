package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.LoanContractCloseBusinessInfo;


public interface ILoanContractCloseBusinessInfoDao extends IBaseDao<LoanContractCloseBusinessInfo>{

   public LoanContractCloseBusinessInfo selectIsFoOrgType(Long employeeId);
   
	public List<Map<String, Object>> selectShutShop();
	
	public List<Map<String, Object>> selectShutedShop();
	
	public int flushShutedShop();
}