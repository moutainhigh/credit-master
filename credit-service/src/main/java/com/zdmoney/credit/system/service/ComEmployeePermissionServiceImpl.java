package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComEmployeePermissionDao;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.IComRolePermissionDao;
import com.zdmoney.credit.system.domain.ComEmployeePermission;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRolePermission;
import com.zdmoney.credit.system.service.pub.IComEmployeePermissionService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ComEmployeePermissionServiceImpl implements
		IComEmployeePermissionService {
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("comEmployeePermissionDaoImpl")
	IComEmployeePermissionDao comEmployeePermissionDao;
	@Autowired
	@Qualifier("comEmployeeRoleDaoImpl")
	IComEmployeeRoleDao comEmployeeRoleDaoImpl;
	@Autowired
	@Qualifier("comRolePermissionDaoImpl")
	IComRolePermissionDao comRolePermissionDao;

	@Override
	public ComEmployeePermission get(ComEmployeePermission comEmployeePermission) {
		return comEmployeePermissionDao.get(comEmployeePermission);
	}

	@Override
	public ComEmployeePermission get(Long id) {
		return comEmployeePermissionDao.get(id);
	}

	@Override
	public Pager findWithPg(ComEmployeePermission comEmployeePermission) {
		return comEmployeePermissionDao.findWithPg(comEmployeePermission);
	}

	@Override
	public ComEmployeePermission insertComEmployeePermission(
			ComEmployeePermission comEmployeePermission) {

		return comEmployeePermissionDao.insert(comEmployeePermission);
	}

	@Override
	public void deleteComEmployeePermission(ComEmployeePermission comEmployeePermission, String pid) {
		comEmployeePermissionDao.deleteByIdList(comEmployeePermission);
		// 获取菜单id。分割成数组
		if (Strings.isNotEmpty(pid)) {
			String perid[] = pid.split(",");
			if (perid.length > 0) {
				for (int i = 0; i < perid.length; i++) {
					if (!"".equals(perid[i])) {
						comEmployeePermission
								.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_EMPLOYEE_PERMISSION));
						comEmployeePermission.setPermId(Long.parseLong(perid[i]));
						comEmployeePermissionDao.insert(comEmployeePermission);
					}
				}
			}
		}
	}

	@Override
	public List findComPermissionIdByEmployeeId(
			ComEmployeePermission comEmployeePermission) {
		List list = (List) comEmployeePermissionDao
				.findListByVo(comEmployeePermission);
		return list;
	}

	@Override
	public String findPerIdByEmpId(ComEmployeePermission comEmployeePermission) {
		ComEmployeeRole comEmployeeRole = new ComEmployeeRole();
		comEmployeeRole.setEmployeeId(comEmployeePermission.getEmpId());
		// 得到所有角色
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
		String jsonRole[] = json.split(",");
		String jsonper = "";
		ComRolePermission comRolePermission = null;
		if (jsonRole != null) {
			for (int j = 0; j < jsonRole.length; j++) {
				if (!jsonRole[j].equals("")) {
					comRolePermission = new ComRolePermission();
					comRolePermission.setRoleId(Long.parseLong(jsonRole[j]));
					List listPer = comRolePermissionDao
							.findListByVo(comRolePermission);
					for (int i = 0; i < listPer.size(); i++) {
						comRolePermission = (ComRolePermission) listPer.get(i);
						if (i == listPer.size() - 1&&j== jsonRole.length-1) {
							jsonper += comRolePermission.getPermId();
						} else {
							jsonper += comRolePermission.getPermId() + ",";
						}
					}
				}
			}
		}
		return jsonper;
	}
}
