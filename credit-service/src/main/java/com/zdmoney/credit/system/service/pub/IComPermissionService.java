package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComPermission;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComPermissionService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param ComPermission 实体信息
	 * @return
	 */
	public ComPermission get(ComPermission comPermission);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComPermission get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComPermission 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComPermission comPermission) ;
	
	/**
	 * 加载所有的一级菜单
	 * @param
	 */
	public List<ComPermission> findComPermission(ComPermission comPermission);
	
	/**
	 * 查询所有菜单及权限数据
	 * @param comPermission
	 * @return
	 */
	public List<ComPermission> findAll();
	
	/***
	 * 通过角色获取权限
	 * @param roleId
	 * @return
	 */
	List<ComPermission> getPermissionByRole(Long roleId);
	
	List<ComPermission> getTopPermissionList(String permType);
	
	/**
	 * 根据用户ID查询所拥有的权限
	 * @param comEmployeeId
	 * @return
	 */
	public List<ComPermission> getPermissionByComEmployee(Long comEmployeeId);
}
