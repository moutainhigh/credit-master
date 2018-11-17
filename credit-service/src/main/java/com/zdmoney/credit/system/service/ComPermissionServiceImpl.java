package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.dao.pub.IComPermissionDao;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.service.pub.IComPermissionService;

@Service
public class ComPermissionServiceImpl implements IComPermissionService {
	@Autowired @Qualifier("comPermissionDaoImpl")
	IComPermissionDao comPermissionDaoImpl;
	
	@Override
	public List<ComPermission> findComPermission(ComPermission comPermission) {
		return comPermissionDaoImpl.findListByVo(comPermission);
	}

	@Override
	public Pager findWithPg(ComPermission comPermission) {
		return comPermissionDaoImpl.findWithPg(comPermission);
	}

	@Override
	public ComPermission get(ComPermission comPermission) {
		return comPermissionDaoImpl.get(comPermission);
	}

	@Override
	public ComPermission get(Long id) {
		return comPermissionDaoImpl.get(id);
	}

	public List<ComPermission> getPermissionByRole(Long roleId) {
		// TODO Auto-generated method stub
		return comPermissionDaoImpl.getPermissionByRole(roleId);
	}

	@Override
	public List<ComPermission> getTopPermissionList(String permType) {
		// TODO Auto-generated method stub
		return comPermissionDaoImpl.getTopPermissionList(permType);
	}

	@Override
	public List<ComPermission> getPermissionByComEmployee(Long comEmployeeId) {
		// TODO Auto-generated method stub
		return comPermissionDaoImpl.getPermissionByComEmployee(comEmployeeId);
	}
	
	@Override
	public List<ComPermission> findAll() {
		ComPermission comPermission = new ComPermission();
		return findComPermission(comPermission);
	}

}
