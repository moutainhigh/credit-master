package com.zdmoney.credit.framework.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.pub.ISystemDao;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * DAO基类，定义一些与系统业务无关的方法
 * @author 00236640
 * @version $Id: SystemDaoImpl.java, v 0.1 2015年11月13日 下午2:59:59 00236640 Exp $
 */
@Repository
public class SystemDaoImpl extends BaseDaoImpl<BaseDomain> implements ISystemDao {

}
