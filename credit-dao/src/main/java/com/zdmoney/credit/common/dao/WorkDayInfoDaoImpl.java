package com.zdmoney.credit.common.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.dao.pub.IWorkDayInfoDao;
import com.zdmoney.credit.common.domain.WorkDayInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class WorkDayInfoDaoImpl extends BaseDaoImpl<WorkDayInfo> implements IWorkDayInfoDao {

	@Override
	public WorkDayInfo getWorkDayInfoByParamsMap(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getWorkDayInfoByParamsMap", params);
	}

	@Override
	public WorkDayInfo getPreviousWorkDayInfoByParams(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getPreviousWorkDayInfoByParams", params);
	}

	@Override
	public WorkDayInfo getAfterWorkDayInfoByParams(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getAfterWorkDayInfoByParams", params);
	}

	@Override
	public Integer getWorkDaysInRegion(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getWorkDaysInRegion", params);
	}
}
