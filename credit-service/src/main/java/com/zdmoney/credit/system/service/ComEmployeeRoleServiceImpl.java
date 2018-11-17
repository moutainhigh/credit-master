package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.IComRoleHierarchyDao;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRoleHierarchy;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ComEmployeeRoleServiceImpl implements IComEmployeeRoleService {

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("comEmployeeRoleDaoImpl")
	IComEmployeeRoleDao comEmployeeRoleDaoImpl;
	@Autowired
	@Qualifier("comRoleHierarachyDaoImpl")
	IComRoleHierarchyDao comRoleHierarachyDaoImpl;
	@Override
	public String findComEmployeeRole(ComEmployeeRole comEmployeeRole) {
		List list = comEmployeeRoleDaoImpl.findListByVo(comEmployeeRole);
		String json = "";
		for (int i = 0; i < list.size(); i++) {
			comEmployeeRole = (ComEmployeeRole) list.get(i);
			if (i == list.size() - 1) {
				json += comEmployeeRole.getRoleId();
			} else {
				json += comEmployeeRole.getRoleId() + ",";
			}
		}
		
		return json;
	}
	
	
	public String findComEmployeeRoleComRoleHierarchy(ComEmployeeRole comEmployeeRole) {
		List list = comEmployeeRoleDaoImpl.findListByVo(comEmployeeRole);
		ComRoleHierarchy comRoleHierarchy=null;
		String sonRoleId="";
		String json = "";
		for (int i = 0; i < list.size(); i++) {
			comEmployeeRole = (ComEmployeeRole) list.get(i);
			if (i == list.size() - 1) {
				json += comEmployeeRole.getRoleId();
			} else {
				json += comEmployeeRole.getRoleId() + ",";
			}
		}
		if(!json.equals("")){
			String[] roleId=json.split(",");
			if(roleId!=null){
				for (int i = 0; i < roleId.length; i++) {
					comRoleHierarchy=new ComRoleHierarchy();
					comRoleHierarchy.setRoleId(Long.parseLong(roleId[i]));
					List<ComRoleHierarchy> listComRoleH=comRoleHierarachyDaoImpl.findListByVo(comRoleHierarchy);
					if(listComRoleH!=null&&listComRoleH.size()>0){
						for (int j = 0; j <listComRoleH.size() ;j++) {
							comRoleHierarchy=listComRoleH.get(j);
							if (j == listComRoleH.size() - 1&&i==roleId.length-1) {
								sonRoleId += comRoleHierarchy.getSonRoleId();
							} else {
								sonRoleId += comRoleHierarchy.getSonRoleId() + ",";
							}
						}
					}
				}
			}
		}
		return sonRoleId;
	}

	@Override
	public Pager findWithPg(ComEmployeeRole comEmployeeRole) {
		return comEmployeeRoleDaoImpl.findWithPg(comEmployeeRole);
	}

	@Override
	public ComEmployeeRole get(ComEmployeeRole comEmployeeRole) {
		return comEmployeeRoleDaoImpl.get(comEmployeeRole);
	}

	@Override
	public ComEmployeeRole get(Long id) {
		return comEmployeeRoleDaoImpl.get(id);
	}

	@Override
	public void deleteComEmployeeRole(ComEmployeeRole comEmployeeRole,
			String pid) {
		// TODO Auto-generated method stub
		comEmployeeRoleDaoImpl.deleteByIdList(comEmployeeRole);
		// 获取权限id。分割成数组
		if (pid != null) {
			String perid[] = pid.split(",");
			if (perid.length > 0) {
				for (int i = 0; i < perid.length; i++) {
					if(!perid[i].equals("")){
					comEmployeeRole.setRoleId(Long.parseLong(perid[i]));
					comEmployeeRole.setId(sequencesServiceImpl
							.getSequences(SequencesEnum.COM_EMPLOYEE_ROLE));
					comEmployeeRoleDaoImpl.insert(comEmployeeRole);
					}
				}

			}
		}
	}

	@Override
	public void insertComEmployeeRole(ComEmployeeRole comEmployeeRole) {
		comEmployeeRole.setId(sequencesServiceImpl
				.getSequences(SequencesEnum.COM_EMPLOYEE_ROLE));
		// TODO Auto-generated method stub
		comEmployeeRoleDaoImpl.insert(comEmployeeRole);
	}
	
	@Override
	public List<ComEmployeeRole> findComEmployeeRoleListByEmpId(ComEmployeeRole comEmployeeRole) {
		
		return comEmployeeRoleDaoImpl.findComEmployeeRoleListByEmpId(comEmployeeRole);
	}
}
