package com.zdmoney.credit.operation.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfoWm;

public interface IBasePublicAccountInfoWmService {
	//public BasePublicAccountInfoWm get(Long id);
	
	public Pager findWithPg(BasePublicAccountInfoWm basePublicAccountInfo);
	 
	public List<BasePublicAccountInfoWm> findListByVo(BasePublicAccountInfoWm basePublicAccountInfo);
	
	public void savePrivateAccountInfo(List<Map<String, String>> sheetDataList, String loanBelong);
	
	public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePublicAccountInfoWm basePrivateAccountInfo);
	
	public int updateAccountInfoForExport(Map<String, Object> params);
	
	public List<BasePublicAccountInfoWm> findQueryResultList(BasePublicAccountInfoWm basePrivateAccountInfo);
	
	 public String checkReceiveStatus();
	 
	 public BasePublicAccountInfoWm get(Long id);
	 
	 public int updateAccountInfoForCancel(BasePublicAccountInfoWm basePrivateAccountInfo);
	 
	 public int updatePrivateAccountInfo(BasePublicAccountInfoWm basePrivateAccountInfo);
	 
	 public Map<String, Object> findAccountReceiveTime();
	 
	 public void updateAccountReceiveTime(Map<String, Object> params);
	 
	 public Pager findWithPgByMap(Map<String, Object> params);

	public void savePrivateAccountHRBHInfo(List<Map<String, String>> sheetDataList);
	/**
	 * 校验和保存线下还款银行流水信息
	 * @param sheetDataList
	 * @param loanBelong
	 */
	public void saveLufaxAccountInfo(List<Map<String, String>> sheetDataList,String loanBelong);

	public List<Map<String,Object>> findQueryResultMapList(BasePublicAccountInfoWm basePrivateAccountInfo);
}	
