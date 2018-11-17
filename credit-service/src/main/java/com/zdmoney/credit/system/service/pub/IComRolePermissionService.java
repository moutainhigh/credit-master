package com.zdmoney.credit.system.service.pub;


import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRolePermission;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComRolePermissionService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param ComPermission 实体信息
	 * @return
	 */
	public ComRolePermission get(ComRolePermission comRolePermission);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComRolePermission get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComPermission 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComRolePermission comRolePermission) ;
	

	
	
	/**
	 * 新增、修改实体数据
	 * @param ComRole
	 * @return
	 */
	public ComRolePermission saveOrUpdate(ComRolePermission comRolePermission);
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById(Long id);
	
	
	/**
	 * list 删除
	 */
	public void deleteByIdList(ComRolePermission comRolePermission);
	
	public void deleteComRolePermission(ComRolePermission comRolePermission,String pid);
	
	/**
	 * 加载所有的角色
	 * @param
	 */
	public String findPermissionRole(ComRolePermission comRolePermission);
	
}
