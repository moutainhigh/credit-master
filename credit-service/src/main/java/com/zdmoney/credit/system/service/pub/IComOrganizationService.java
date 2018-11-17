package com.zdmoney.credit.system.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.system.domain.ComOrganization;


/**
 * 营业网点、组织架构表Service层接口定义
 * @author Ivan
 *
 */
public interface IComOrganizationService {
	
	/***
	 * 查询营业网点数据（带完整名称 如/证大投资/东区/济南分部/济南/济南解放路营业部）
	 * @param comOrganization
	 * @return
	 */
	public List<ComOrganization> searchWithFullName(ComOrganization comOrganization);
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 * @param comOrganization 实体对象
	 * @author Ivan
	 */
	public List<ComOrganization> findListByVo(ComOrganization comOrganization);
	
//	/**
//	 * 查询实体信息在数据库中是否存在
//	 * @param comEmployee 实体信息
//	 * @return
//	 */
//	public ComEmployee get(ComEmployee comEmployee);
//	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComOrganization get(Long id);
	
	/**
	 * 新增、修改营业网点数据
	 * @param comOrganization
	 * @return
	 */
	public ComOrganization saveOrUpdate(ComOrganization comOrganization);
	
	/**
	 * 删除营业网点数据
	 * @param comOrganization
	 * @return
	 */
	public ComOrganization delete(Long id);
	
	/**
     * 查询当前机构的父机构信息
     * @param id
     * @return
     */
    public ComOrganization findParentOrganization(Long id);
    
	/**
	 * 根据机构层级查询机构信息
	 * @param employeeType
	 * @return
	 */
	public Map<String,Object> findOrganizationByLevel(String vlevel,boolean showParentName);
	
	/**
	 * 更改营业网点归属关系
	 * @param comOrganization
	 */
	public void changeOrgLevel(ComOrganization comOrganization);
	
	/**
	 * 查询营业网点完整名称
	 */
	public String searchOrgFullName(Long orgId);
	
	public List<Map<String, Object>> findOrganization(Map<String, Object> params);
	
	public Integer getPaymentRouteDeptCount(Long orgId);
}
