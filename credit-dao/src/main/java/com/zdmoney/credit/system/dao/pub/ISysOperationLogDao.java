package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.SysOperationLog;

public interface ISysOperationLogDao extends IBaseDao<SysOperationLog>{
	
	/**
	 * 通过token和员工编号更新操作日志
	 * @param sysOperationLog
	 * @return
	 */
	public int updateByEmployeeIdAndToken(SysOperationLog sysOperationLog);
	
	public SysOperationLog selectByEmployeeIdAndToken(SysOperationLog sysOperationLog);
}
