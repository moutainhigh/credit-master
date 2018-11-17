package com.zdmoney.credit.system.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComPermission;

/**
 * 员工数据表Service接口定义
 * @author Ivan
 *
 */
public interface IComEmployeeService {
	
	/**
	 * 查询实体信息在数据库中是否存在
	 * @param comEmployee 实体信息
	 * @return
	 */
	public ComEmployee get(ComEmployee comEmployee);
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public ComEmployee get(Long id) ;
	
	/**
	 * 带分页查询
	 * @param ComEmployee 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ComEmployee comEmployee) ;
	
	/**
	 * 加载所有的一级菜单
	 * @param
	 */
	public List findComPermission(ComPermission comPermission);
	

	/**
	 * 新增、修改员工
	 * @param comEmployee 员工信息
	 * @return
	 */
	public ComEmployee saveOrUpdate(ComEmployee comEmployee);
	
	/**
	 * 新增、修改员工
	 * @param comEmployee 员工信息
	 * @param isSync 是否同步到第三方系统
	 * @return
	 */
	public ComEmployee saveOrUpdate(ComEmployee comEmployee,boolean isSync);
	
	/**
	 * 新增、修改员工
	 * @param comEmployee 员工信息
	 * @param isSync 是否同步到第三方系统
	 * @param isEncrypt 是否方法内做MD5密码加密
	 * @return
	 */
	public ComEmployee saveOrUpdate(ComEmployee comEmployee,boolean isOldCall,boolean isNewCall,boolean isEncrypt);
	
	

	public List getComEmployeeList();
	
	/**
	 * 
	 * 根据营业机构Id和角色名查询客服信息
	 * @param params
	 * @return
	 */
    public ComEmployee queryEmployeeByOrgIdAndRoleName(Map<String,Object> params);

    /**
     * 根据userCode查询
     * @param userCode
     * @return
     */
	public ComEmployee findByUserCode(String userCode);
	
	/**
	 * @author 00236633
	 * @param employeeType
	 * @return
	 */
	public Map<String,Object> findEmployeeByEmployeeType(String employeeType);
	
	/**
	 * 查询员工(带分页)
	 * @author Ivan
	 * @param params
	 * @return list
	 */
	public Pager findEmployeeWithPg(Map<String, Object> params);
	/**
	 * 查询员工(带分页)
	 * @author Ivan
	 * @param params
	 * @return list
	 */
	public Pager findWithPgByMap(Map paramMap);
	
	

	public ComEmployee findComEmployeeByUser(ComEmployee comEmployee);
	
	public void forgetPwd(ComEmployee employee) throws Exception;
	
	public void resetPwd(ComEmployee employee, String newPwd, String token, String ip) throws Exception;
	
	/**
	 * 跟据营业网点编号查询员工数据
	 * @param orgId 营业网点编号
	 * @return
	 */
	public List<ComEmployee> findListByOrgId(Long orgId);
	
	/**
	 * 批量导入
	 */
	public  void saveEmpAndRole(ComEmployee comEmployee,Long roleId);
	/**
	 * 查询某门店下所有已离职的客户经理和客服列表数据
	 * @param orgCode
	 * @return
	 */
	public List<Map<String,String>> findLeaveEmployeeList(String orgCode);
}
