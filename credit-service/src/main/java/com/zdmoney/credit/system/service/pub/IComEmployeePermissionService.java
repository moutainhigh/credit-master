package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComEmployeePermission;
import com.zdmoney.credit.system.domain.ComPermission;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComEmployeePermissionService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param ComEmployeePermission 实体信息
	 * @return
	 */
	public ComEmployeePermission get(ComEmployeePermission comEmployeePermission);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComEmployeePermission get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComEmployeePermission 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComEmployeePermission comEmployeePermission) ;
	
	/**
	 * 新增用户菜单
	 */
	public ComEmployeePermission insertComEmployeePermission(ComEmployeePermission comEmployeePermission);
	
	/**
	 * 删除用户菜单
	 */
	public void deleteComEmployeePermission(ComEmployeePermission comEmployeePermission,String pid);
	
	
	/**
	 * 根据用户id查询所有菜单的id
	 * 
	 */
	public List findComPermissionIdByEmployeeId(ComEmployeePermission comEmployeePermission);

	public String findPerIdByEmpId(ComEmployeePermission comEmployeePermission);
	
	
	
}
