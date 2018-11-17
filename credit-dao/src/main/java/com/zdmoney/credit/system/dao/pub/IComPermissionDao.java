package com.zdmoney.credit.system.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ComPermission;

/**
 * 员工数据表Dao接口定义
 * @author Ivan
 *
 */
public interface IComPermissionDao extends IBaseDao<ComPermission>{
	/**
	 * 通过角色ID，查找权限
	 * @param roleId
	 * @return
	 */	
	public List<ComPermission> getPermissionByRole(Long roleId);
	
	/**
	 * 查找顶级菜单权限
	 * @param permType
	 * @return
	 */
	public List<ComPermission> getTopPermissionList(String permType);
	
	/**
	 * 通过员工ID，查找权限   
	 * com_employee_role
	 * @param comEmployeeId
	 * @return
	 */
	public List<ComPermission> getPermissionByComEmployee(Long comEmployeeId);

}
