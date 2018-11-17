package com.zdmoney.credit.system.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.ISysOperationLogDao;
import com.zdmoney.credit.system.domain.SysOperationLog;

@Repository
public class SysOperationLogDaoImpl extends BaseDaoImpl<SysOperationLog> implements ISysOperationLogDao{
	
	@Override
	public int updateByEmployeeIdAndToken(SysOperationLog sysOperationLog) {
		sysOperationLog.setUpdateTime(new Date());
		return getSqlSession().update(getIbatisMapperNameSpace() + ".updateByEmployeeIdAndToken", sysOperationLog);
	}

	@Override
	public SysOperationLog selectByEmployeeIdAndToken(
			SysOperationLog sysOperationLog) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".selectByEmployeeIdAndToken", sysOperationLog);
	}

}
