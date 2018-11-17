package com.zdmoney.credit.overdue.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.overdue.dao.pub.IOverdueRatioStatDao;
import com.zdmoney.credit.overdue.domain.OverdueRatioStat;
@Repository
public class OverdueRatioStatDaoImpl extends BaseDaoImpl<OverdueRatioStat> implements IOverdueRatioStatDao{

}
