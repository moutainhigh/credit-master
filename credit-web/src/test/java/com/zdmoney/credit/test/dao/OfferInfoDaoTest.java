package com.zdmoney.credit.test.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration
({"/spring/*.xml"}) //加载配置文件
public class OfferInfoDaoTest {
	@Autowired 
	IOfferInfoDao offerInfoDao;
	
	@Test
	@Rollback(false)
	public void testUpdateByPrimaryKeySelective () {
		OfferInfo offerInfo = new OfferInfo();
		offerInfo.setId(244088l);
		offerInfo.setState(OfferStateEnum.已报盘.getValue());
		offerInfoDao.updateByPrimaryKeySelective(offerInfo);
	}
}
