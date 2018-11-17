package com.zdmoney.credit.app.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.app.dao.pub.IAppNoticeDao;
import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class AppNoticeDaoImpl extends BaseDaoImpl<AppNotice> implements IAppNoticeDao {

}
