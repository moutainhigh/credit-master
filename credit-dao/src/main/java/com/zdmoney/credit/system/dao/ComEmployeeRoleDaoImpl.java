package com.zdmoney.credit.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.domain.ComEmployeeRole;

@Repository
public class ComEmployeeRoleDaoImpl extends BaseDaoImpl<ComEmployeeRole>
		implements IComEmployeeRoleDao {

	@Override
	public List<ComEmployeeRole> findComEmployeeRoleListByEmpId(ComEmployeeRole comEmployeeRole) {
		
		
		 
		 return getSqlSession().selectList(
					getIbatisMapperNameSpace() + ".findComEmployeeRoleListByEmpId",
					comEmployeeRole);
	}

	public int findComEmployeeRoleByUserCodeAndRoleName(Map<String,Object> params){
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findComEmployeeRoleByUserCodeAndRoleName",params);
	}
}
