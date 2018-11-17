package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.app.service.pub.IAppNoticeService;
import com.zdmoney.credit.common.util.Pager;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
// 加载配置文件
public class AppNoticeTest {

	@Autowired
	IAppNoticeService appNoticeServiceImpl;

	@Test
	public void findWithPg() {

		AppNotice appNotice = new AppNotice();
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(5);
		pager.setSidx("CREATE_TIME");
		pager.setSort("DESC");
		appNotice.setPager(pager);
		pager = appNoticeServiceImpl.findWithPg(appNotice);
		System.out.println(pager.getTotalCount());
		System.out.println(pager.getResultList());
	}
}
