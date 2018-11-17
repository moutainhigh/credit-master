package com.zdmoney.credit.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.IComRoleDao;
import com.zdmoney.credit.system.dao.pub.IComRoleHierarchyDao;
import com.zdmoney.credit.system.dao.pub.IComRolePermissionDao;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.domain.ComRoleHierarchy;
import com.zdmoney.credit.system.domain.ComRolePermission;
import com.zdmoney.credit.system.service.pub.IComRoleService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ComRoleServiceImpl implements IComRoleService {
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired
	@Qualifier("comRoleDaoImpl")
	IComRoleDao comRoleDaoImpl;
	
	@Autowired
	@Qualifier("comRolePermissionDaoImpl")
	IComRolePermissionDao comRolePermissionDaoImpl;
	
	@Autowired
	@Qualifier("comEmployeeRoleDaoImpl")
	IComEmployeeRoleDao comEmployeeRoleDaoImpl;
	
	@Autowired
	@Qualifier("comRoleHierarachyDaoImpl")
	IComRoleHierarchyDao comRoleHierarachyDaoImpl;
	@Override
	public List findComRole(ComRole comRole) {

		return comRoleDaoImpl.findListByVo(comRole);
	}

	@Override
	public Pager findWithPg(ComRole comRole) {
		// TODO Auto-generated method stub
		return comRoleDaoImpl.findWithPg(comRole);
	}

	@Override
	public ComRole get(Long id) {
		// TODO Auto-generated method stub
		return comRoleDaoImpl.get(id);
	}

	@Override
	public ComRole get(ComRole comRole) {
		// TODO Auto-generated method stub
		return comRoleDaoImpl.get(comRole);
	}
	
	/**
	 * 新增、修改实体数据
	 * @param myTest
	 * @return
	 */
	@Override
	public ComRole saveOrUpdate(ComRole comRole) {
		if (comRole == null) {
			return comRole;
		}
		Long id = comRole.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			comRole.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_ROLE));
			comRole.setIsActive("1");//表示有效。
			comRoleDaoImpl.insert(comRole);
		} else {
			/** 修改操作 **/
			User user = UserContext.getUser();
			comRole.setUpdator(user.getId()+"");
			comRole.setUpdateTime(new Date());
			comRoleDaoImpl.update(comRole);
		}
		return comRole;
	}
	@Override
	public void deleteById(Long id) {
		comRoleDaoImpl.deleteById(id);
		//删除中间表数据,角色权限表，角色菜单表
		ComRolePermission comrolePermission=new ComRolePermission();
		comrolePermission.setRoleId(id);
		comRolePermissionDaoImpl.deleteByIdList(comrolePermission);
		ComEmployeeRole comemployeeRole=new ComEmployeeRole();
		comemployeeRole.setRoleId(id);
		comEmployeeRoleDaoImpl.deleteByIdList(comemployeeRole);
	}

	@Override
	public List<ComRole> getRolesByUser(Long employeeId) {
		// TODO Auto-generated method stub
		return comRoleDaoImpl.getRolesByUser(employeeId);
	}
	
	@Override
	public ComRole getIsThere(ComRole comRole) {
		// TODO Auto-generated method stub
		return comRoleDaoImpl.getIsThere(comRole);
	}
	
	@Override
	public String findRoleHierachy(ComRoleHierarchy comRoleHierarchy) {
		String json="";
		List<ComRoleHierarchy> list = comRoleHierarachyDaoImpl.findListByVo(comRoleHierarchy);
		if(list!=null&&list.size()>0){
			for (int i = 0; i <list.size(); i++) {
				comRoleHierarchy=list.get(i);
				if (i == list.size() - 1) {
					json += comRoleHierarchy.getSonRoleId();
				} else {
					json += comRoleHierarchy.getSonRoleId() + ",";
				}
			}
		}
		return json;
	}
	
	@Override
	public void deleteComRoleHierarchy(String pid, Long roleId) {
		ComRoleHierarchy comRoleHierarchy=new ComRoleHierarchy();
		comRoleHierarchy.setRoleId(roleId);
		comRoleHierarachyDaoImpl.deleteByIdList(comRoleHierarchy);
	ComRole comrole=	comRoleDaoImpl.get(roleId);
	comRoleHierarchy.setRoleName(comrole.getRoleName());
		if(pid != null){
			String[] pids=pid.split(",");
			if(pids!=null){
				for (int i = 0; i < pids.length; i++) {
				ComRole comSonrole=	comRoleDaoImpl.get(Long.parseLong(pids[i]));
					comRoleHierarchy.setSonRoleId(Long.parseLong(pids[i]));
					comRoleHierarchy.setSonRoleName(comSonrole.getRoleName());
					comRoleHierarchy.setId(sequencesServiceImpl
							.getSequences(SequencesEnum.COM_ROLE_HIERARCHY));
					comRoleHierarachyDaoImpl.insert(comRoleHierarchy);
				}
			}
		}
			
	}
	
	
}
