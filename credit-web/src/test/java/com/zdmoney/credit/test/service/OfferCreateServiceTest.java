package com.zdmoney.credit.test.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class OfferCreateServiceTest {
	
	@Autowired 
	IOfferCreateService offerCreateService;
	
	@Test
	public void testateOffer () {
		
//		VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(30696572l);
		OfferParamsVo offerParamsVo = new OfferParamsVo();
		offerParamsVo.setOfferAmount(new BigDecimal(220));
		offerParamsVo.setLoanId(45507631l);
		offerCreateService.createRealtimeOfferInfo(offerParamsVo);
		
	}
}
