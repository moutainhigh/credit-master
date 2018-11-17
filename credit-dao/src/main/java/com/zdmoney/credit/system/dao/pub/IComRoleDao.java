package com.zdmoney.credit.system.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ComRole;

/**
 * 权限表
 * @author Ivan
 *
 */
public interface IComRoleDao extends IBaseDao<ComRole>{
	public List<ComRole> getRolesByUser(Long employeeId);

	public ComRole getIsThere(ComRole comRole);
}
