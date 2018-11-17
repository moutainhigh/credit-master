package com.zdmoney.credit.system.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ComEmployee;

/**
 * 员工数据表Dao接口定义
 * @author Ivan
 *
 */
public interface IComEmployeeDao extends IBaseDao<ComEmployee>{
    
    /**
     * 
     * 根据营业机构Id和角色名查询客服信息
     * @param params
     * @return
     */
    public ComEmployee queryEmployeeByOrgIdAndRoleName(Map<String,Object> params);
    
    
    /**
     * 根据userCode获取员工信息
     * @param userCode 员工编号
     * @return 员工信息对象
     */
    public ComEmployee findEmployeeByUserCode(String userCode);
    
    /**
     * 查询员工
     * @author 00236633
     * @param params
     * @return list
     */
    public List<Map<String,Object>> findEmployee(Map<String,Object> params);
    
    /**
     * 查询员工(带分页)
     * @author Ivan
     * @param params
     * @return list
     */
    public Pager findEmployeeWithPg(Map<String,Object> params);


    public ComEmployee findEmployeeByUserCode(ComEmployee comEmployee);

    /**
     * 根据工号和角色名查询员工信息
     * @param params
     * @return
     */
    public List<ComEmployee> queryEmployeeByUsercodeAndRoleName(Map<String, Object> params);

    /**
     * 查询某门店下所有已离职的客户经理列表数据
     * @param orgCode
     * @return
     */
	public List<Map<String, String>> findLeaveEmployeeList(String orgCode);

	/**
	 * 查询门店下所有的营业部经理/副理/客服
	 * @param orgId
	 * @return
	 */
	public List<ComEmployee> finListByOrgId(Long orgId);

	/**
	 * 查询门店下所有的营业部经理/副理
	 * @param orgId
	 * @return
	 */
	public List<ComEmployee> finJlFlByOrgId(Long orgId);
}
