package com.zdmoney.credit.app.service.pub;

import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.common.util.Pager;

public interface IAppNoticeService {
	public Pager findWithPg(AppNotice appNotice);

	/**
	 * 新增、修改公告数据
	 * 
	 * @param comOrganization
	 * @return
	 */
	public AppNotice saveOrUpdate(AppNotice appNotice);

	/**
	 * 查询公告信息
	 * 
	 * @param id 公告编号
	 * @return
	 */
	public AppNotice findById(Long id);
}
