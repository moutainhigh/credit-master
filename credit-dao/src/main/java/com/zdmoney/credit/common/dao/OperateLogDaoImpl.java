package com.zdmoney.credit.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.dao.pub.IOperateLogDao;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class OperateLogDaoImpl extends BaseDaoImpl<OperateLog> implements IOperateLogDao {

	
	@Override
	public List<OperateLog> findOperateLogList2DownLoad(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOperateLogList2DownLoad", params);
	}

	@Override
	public List<Long> findLoanIds4FormalRepayPlans2Lufax() {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanIds4FormalRepayPlans2Lufax");
	}
}
