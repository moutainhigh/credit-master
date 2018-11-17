package com.zdmoney.credit.system.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.ISysActionLogDao;
import com.zdmoney.credit.system.domain.SysActionLog;

@Repository
public class SysActionLogDaoImpl extends BaseDaoImpl<SysActionLog> implements ISysActionLogDao{

}
