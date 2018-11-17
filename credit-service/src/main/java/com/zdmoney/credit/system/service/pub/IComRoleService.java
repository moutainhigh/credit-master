package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.domain.ComRoleHierarchy;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComRoleService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param ComPermission 实体信息
	 * @return
	 */
	public ComRole get(ComRole comRole);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComRole get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComPermission 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComRole comRole) ;
	
	/**
	 * 加载所有的角色
	 * @param
	 */
	public List findComRole(ComRole comRole);
	
	
	/**
	 * 新增、修改实体数据
	 * @param ComRole
	 * @return
	 */
	public ComRole saveOrUpdate(ComRole comRole);
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById(Long id);
	
	
	public List<ComRole> getRolesByUser(Long employeeId);

	public ComRole getIsThere(ComRole comRole);

	public String findRoleHierachy(ComRoleHierarchy comRoleHierarchy);

	public void deleteComRoleHierarchy(String pid, Long roleId);
}
