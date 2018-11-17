package com.zdmoney.credit.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.app.dao.pub.IAppNoticeDao;
import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.app.service.pub.IAppNoticeService;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class AppNoticeServiceImpl implements IAppNoticeService {
	@Autowired
	@Qualifier("appNoticeDaoImpl")
	IAppNoticeDao appNoticeDaoImpl;

	@Autowired
	ISequencesService sequencesServiceImpl;

	@Override
	public Pager findWithPg(AppNotice appNotice) {
		return appNoticeDaoImpl.findWithPg(appNotice);
	}

	@Override
	public AppNotice saveOrUpdate(AppNotice appNotice) {
		Long id = appNotice.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/

			/** 获取Sequences **/
			id = sequencesServiceImpl.getSequences(SequencesEnum.APP_NOTICE);
			appNotice.setId(id);
			appNoticeDaoImpl.insert(appNotice);
		} else {
			/** 修改操作 **/
			appNoticeDaoImpl.update(appNotice);
		}
		return appNotice;
	}

	@Override
	public AppNotice findById(Long id) {
		if (Strings.isEmpty(id)) {
			return null;
		}
		return appNoticeDaoImpl.get(id);
	}

}
