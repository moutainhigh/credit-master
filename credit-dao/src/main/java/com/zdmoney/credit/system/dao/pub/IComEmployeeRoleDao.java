package com.zdmoney.credit.system.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.domain.ComRole;

/**
 * 员工数据表Dao接口定义
 * @author Ivan
 *
 */
public interface IComEmployeeRoleDao extends IBaseDao<ComEmployeeRole>{

	List<ComEmployeeRole> findComEmployeeRoleListByEmpId(ComEmployeeRole comEmployeeRole);
	int findComEmployeeRoleByUserCodeAndRoleName(Map<String,Object> params);
}
