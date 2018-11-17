package com.zdmoney.credit.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IComPermissionDao;
import com.zdmoney.credit.system.domain.ComPermission;
@Repository
public class ComPermissionDaoImpl extends BaseDaoImpl<ComPermission> implements IComPermissionDao{

	@Override
	public List<ComPermission> getPermissionByRole(Long roleId) {
		// TODO Auto-generated method stub
		//getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateMessageStateDelete", list);
		return getSqlSession().selectList(getIbatisMapperNameSpace() +".getPermissionByRole", roleId);
	}

	@Override
	public List<ComPermission> getTopPermissionList(String permType) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(getIbatisMapperNameSpace() +".getTopPermissionList", permType);
	}

	@Override
	public List<ComPermission> getPermissionByComEmployee(Long comEmployeeId) {
		
		return getSqlSession().selectList(getIbatisMapperNameSpace() +".getPermissionByComEmployee", comEmployeeId);
	}
}
