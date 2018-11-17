package com.zdmoney.credit.test.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class OfferInfoServiceTest{
	
	@Autowired 
	IOfferInfoService offerInfoService;
	@Autowired
	private IOfferCreateService offerCreateService;
	@Test
	public void testGetTQJQLoanByDate () {
		
		ComOrganization org = new ComOrganization();
//		org.setCode("0104004400050");
		
		int i = offerCreateService.createOfferRecordWithOrgAndType(org, IOfferInfoService.ZHENGCHANG);
		
		System.out.println(i);
	}
}
