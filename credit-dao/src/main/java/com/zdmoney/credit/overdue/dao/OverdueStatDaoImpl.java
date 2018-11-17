package com.zdmoney.credit.overdue.dao;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.overdue.dao.pub.IOverdueStatDao;
import com.zdmoney.credit.overdue.domain.OverdueStat;
import org.springframework.stereotype.Repository;

/**
 * 逾期比例表DAO
 * Created by 00235304 on 2015/9/29.
 */
@Repository
public class OverdueStatDaoImpl extends BaseDaoImpl<OverdueStat> implements IOverdueStatDao{


}
