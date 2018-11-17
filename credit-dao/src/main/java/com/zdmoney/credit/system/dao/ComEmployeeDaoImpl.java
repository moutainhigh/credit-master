package com.zdmoney.credit.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.domain.ComEmployee;

/**
 * 员工数据表Dao操作层
 * @author Ivan
 *
 */
@Repository
public class ComEmployeeDaoImpl extends BaseDaoImpl<ComEmployee> implements IComEmployeeDao {

    public ComEmployee queryEmployeeByOrgIdAndRoleName(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryEmployeeByOrgIdAndRoleName",params);
    }

    @Override
    public ComEmployee findEmployeeByUserCode(String userCode) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findEmployeeByUserCode",userCode);
    }
    
    @Override
    public ComEmployee findEmployeeByUserCode(ComEmployee comEmployee) {
        // TODO Auto-generated method stub
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findEmployeeByUserCodeAndID",comEmployee);
    }
    /**
     * 查询员工
     * @author 00236633
     * @param params
     * @return list
     */
    @Override
    public List<Map<String, Object>> findEmployee(Map<String, Object> params) {
        List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findEmployee", params);
        return result;
    }
    
    /**
     * 查询员工(带分页)
     * @author Ivan
     * @param params
     * @return list
     */
    @Override
    public Pager findEmployeeWithPg(Map<String,Object> params) {
        Pager pager = (Pager)params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findEmployeeWithPg");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".findEmployeeCount");
        return doPager(pager,params);
    }
    
    public List<ComEmployee> queryEmployeeByUsercodeAndRoleName(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryEmployeeByUsercodeAndRoleName",params);
    }

	@Override
	public List<Map<String, String>> findLeaveEmployeeList(String orgCode) {
		 Map<String, Object> params =new HashMap<String, Object>();
		 params.put("orgCode", orgCode);
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLeaveEmployeeList",params);
	}

	@Override
	public List<ComEmployee> finListByOrgId(Long orgId) {
		
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".finListByOrgId",orgId);
	}

	@Override
	public List<ComEmployee> finJlFlByOrgId(Long orgId) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".finJlFlByOrgId",orgId);
	}
    
    
}
