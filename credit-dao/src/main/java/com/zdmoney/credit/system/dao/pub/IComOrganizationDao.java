package com.zdmoney.credit.system.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ComOrganization;

/**
 * 营业网点、组织架构表Dao层接口定义
 * 
 * @author Ivan
 *
 */
public interface IComOrganizationDao extends IBaseDao<ComOrganization> {

	/***
	 * 查询营业网点数据（带完整名称 如/证大投资/东区/济南分部/济南/济南解放路营业部）
	 * 
	 * @param comOrganization
	 * @return
	 */
	public List<ComOrganization> searchWithFullName(ComOrganization comOrganization);

	/**
	 * 查询最大CODE记录
	 * 
	 * @param comOrganization
	 * @return
	 */
	public ComOrganization getMaxCode(String vLevel);

	/**
	 * 查询当前机构的父机构信息
	 * 
	 * @param id
	 * @return
	 */
	public ComOrganization findParentOrganization(Long id);

	/**
	 * 查询机构
	 * 
	 * @author 00236633
	 * @param params
	 * @return List
	 */
	public List<Map<String, Object>> findOrganization(Map<String, Object> params);

	/***
	 * 批量变更营业网点代码 包含所有子集
	 * 
	 * @param param
	 */
	public void updateBatchOrgCode(Map<String, String> param);

	/***
	 * 批量变更营业网点名称 包含所有子集
	 * 
	 * @param param
	 */
	public void updateBatchFullName(Map<String, String> param);

	/**
	 * 查询营业网点各项数据
	 * 
	 * @param id
	 * @return
	 */
	public Map queryPartOrgName(Long id);

	/**
	 * 获取已停业的营业部网点
	 * @param id
	 * @return
	 */
	public ComOrganization getNoValidSalesDepartment(Long id);
	
	public Integer getPaymentRouteDeptCount(Long orgId);
}
