package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanFilesInfo;

public interface ILoanFilesInfoDao extends IBaseDao<LoanFilesInfo>{
	/**
	 * 根据登录用户Id查询业务人员
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findBusinessEmployeeListByOrgId(Map<String,Object> params);

	/**
	 * 查询客户档案列表
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findLoanFilesInfoListByMap(Map<String,Object> params);
	
	/**
	 * 查询客户档案列表数量
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public int findLoanFilesInfoCountByMap(Map<String,Object> params);
	
	/**
	 * 根据员工号查询员工
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findEmployeeListByIds(Map<String,Object> params);
	
	/**
	 * 查询借款基本信息
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findLoanBaseInfoById(Map<String,Object> params);
	
	/**
	 * 查询档案袋使用的编号
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findBorrowDocNumByLoanId(Map<String,Object> params);
	
	/**
	 * 添加客户档案
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public int insert(Map<String,Object> params);
	
	/**
	 * 查询档案管理信息根据档案id
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findLoanFilesInfoById(Map<String,Object> params);
	
	/**
	 * 更新客户档案
	 * @author 00236633
	 * @param params
	 * @return
	 */
	public int update(Map<String,Object> params);

	/**
	 * 查询档案催收管理列表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findLoanCollectionManageListByMap(Map<String, Object> params);

	/**
	 * 查询档案催收管理表数据总计
	 * @param params
	 * @return
	 */
	public int findLoanCollectionManageCountByMap(Map<String, Object> params);

	/**
	 * 更新档案催收信息
	 * @param params
	 * @return
	 */
	public int updateCollectionByIds(Map<String, Object> params);

	/**
	 * 根据条件获取档案催收信息
	 * @param params
	 * @return
	 */
	public List getLoanCollectionByMap(Map<String, Object> params);

	/**
	 * 插入档案催收信息数据
	 * @param params
	 * @return 
	 */
	public int insertLoanCollection(Map<String, Object> params);

}
