package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComRolePermissionDao;
import com.zdmoney.credit.system.domain.ComRolePermission;
import com.zdmoney.credit.system.service.pub.IComRolePermissionService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ComRolePermissionServiceImpl implements IComRolePermissionService {
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	@Qualifier("comRolePermissionDaoImpl")
	IComRolePermissionDao comRolePermissionDao;

	@Override
	public ComRolePermission get(ComRolePermission comRolePermission) {
		return comRolePermissionDao.get(comRolePermission);
	}

	@Override
	public ComRolePermission get(Long id) {
		return comRolePermissionDao.get(id);
	}

	@Override
	public Pager findWithPg(ComRolePermission comRolePermission) {
		return comRolePermissionDao.findWithPg(comRolePermission);
	}

	@Override
	public ComRolePermission saveOrUpdate(ComRolePermission comRolePermission) {
		return comRolePermissionDao.insert(comRolePermission);
	}

	@Override
	public void deleteById(Long id) {
		comRolePermissionDao.deleteById(id);

	}

	@Override
	public void deleteByIdList(ComRolePermission comRolePermission) {
		comRolePermissionDao.deleteByIdList(comRolePermission);
	}

	@Override
	public void deleteComRolePermission(ComRolePermission comRolePermission, String pid) {
		comRolePermissionDao.deleteByIdList(comRolePermission);
		// 获取菜单id。分割成数组
		if (Strings.isNotEmpty(pid)) {
			String perid[] = pid.split(",");
			if (perid.length > 0) {
				for (int i = 0; i < perid.length; i++) {
					if (!"".equals(perid[i])) {
						comRolePermission.setPermId(Long.parseLong(perid[i]));
						comRolePermission.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_ROLE_PERMISSION));
						comRolePermissionDao.insert(comRolePermission);
					}
				}
			}
		}
	}

	@Override
	public String findPermissionRole(ComRolePermission comRolePermission) {
		List<ComRolePermission> list = comRolePermissionDao.findListByVo(comRolePermission);
		String json = "";
		for (int i = 0; i < list.size(); i++) {
			comRolePermission = (ComRolePermission) list.get(i);
			if (i == list.size() - 1) {
				json += comRolePermission.getPermId();
			} else {
				json += comRolePermission.getPermId() + ",";
			}
		}
		return json;
	}
}
