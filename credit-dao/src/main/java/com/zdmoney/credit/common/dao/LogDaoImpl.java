package com.zdmoney.credit.common.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.dao.pub.ILogDao;
import com.zdmoney.credit.common.domain.Log;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class LogDaoImpl extends BaseDaoImpl<Log> implements ILogDao {
    
}
