package com.zdmoney.credit.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IComRoleDao;
import com.zdmoney.credit.system.domain.ComRole;
@Repository
public class ComRoleDaoImpl extends BaseDaoImpl<ComRole> implements IComRoleDao{

	@Override
	public List<ComRole> getRolesByUser(Long employeeId) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".getRolesByUser", employeeId);
	}
	
@Override
public ComRole getIsThere(ComRole comRole) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".getIsThere", comRole);
}

}
