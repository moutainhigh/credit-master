package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRole;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComEmployeeRoleService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param ComPermission 实体信息
	 * @return
	 */
	public ComEmployeeRole get(ComEmployeeRole comEmployeeRole);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComEmployeeRole get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComPermission 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComEmployeeRole comEmployeeRole) ;
	
	/**
	 * 加载所有的角色
	 * @param
	 */
	public String findComEmployeeRole(ComEmployeeRole comEmployeeRole);
	
	
	/**
	 * 根据用户id删除角色
	 */
	public void deleteComEmployeeRole(ComEmployeeRole comEmployeeRole,String pid);
	/**
	 * 新增用户角色关系
	 */
	public void insertComEmployeeRole(ComEmployeeRole comEmployeeRole);
	
	
	/**
	 * 根据用户id查询所有角色
	 */
	public List<ComEmployeeRole> findComEmployeeRoleListByEmpId(ComEmployeeRole comEmployeeRole);
	public String findComEmployeeRoleComRoleHierarchy(ComEmployeeRole comEmployeeRole);
}
