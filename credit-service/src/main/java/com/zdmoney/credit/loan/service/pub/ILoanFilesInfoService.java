package com.zdmoney.credit.loan.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.vo.VLoanCollectionInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfo;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoBase;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoUpdate;

public interface ILoanFilesInfoService {
	
	/**
	 * 进入查询界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	public Map<String,Object> listPage();
	
	/**
	 * 查询客户档案信息列表
	 * @author 00236633
	 * @param vLoanFilesInfoList
	 * @param user
	 * @return
	 */
	public Map<String,Object> list(VLoanFilesInfoList vLoanFilesInfoList);
	
	/**
	 * 进入添加界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	public Map<String,Object> addPage(VLoanFilesInfoBase vLoanFilesInfoBase);
	
	/**
	 * 添加客户档案
	 * @author 00236633
	 * @param vLoanFilesInfo
	 * @param user
	 * @return
	 */
	public Map<String,Object> add(VLoanFilesInfo vLoanFilesInfo);
	
	/**
	 * 进入更新界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	public Map<String,Object> updatePage(VLoanFilesInfoBase vLoanFilesInfoBase);
	
	/**
	 * 更新客户档案
	 * @author 00236633
	 * @param vLoanFilesInfo
	 * @param user
	 * @return
	 */
	public Map<String,Object> update(VLoanFilesInfoUpdate vLoanFilesInfoUpdate);
	
	/**
	 * 查询客户档案根据loanId
	 * @author 00236633
	 * @param vLoanFilesInfoList
	 * @param user
	 * @return
	 */
	public Map<String,Object> detail(VLoanFilesInfoBase vLoanFilesInfoBase);

	/**
	 * 查询 档案催收管理列表
	 * @param vLoanCollectionInfoList
	 * @return
	 */
	public Map<String, Object> getLoanCollectionManageList(
			VLoanCollectionInfoList vLoanCollectionInfoList);

	/**
	 * 更新 档案催收信息
	 * @param idList
	 * @param operateType
	 */
	public int updateCollectionByIds(Map<String, Object> params);

	/**
	 * 检查档案催收信息是否存在
	 * @param idList
	 */
	public void checkCollectionExist(List<Long> idList);
}
