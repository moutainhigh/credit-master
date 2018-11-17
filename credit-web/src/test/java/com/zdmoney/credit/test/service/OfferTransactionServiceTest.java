package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.vo.TppCallBackData;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"})
public class OfferTransactionServiceTest {
	@Autowired 
	IOfferTransactionService offerTransactionService;
	
	
	
	
	@Test
	/**
	 * 测试回盘处理
	 */
	public void testOfferBack () {
		TppCallBackData objects = new TppCallBackData();
		objects.setOrderNo("413325");
		objects.setReturnCode(TPPHelper.RESULT_SUCCESS_CODE);
		objects.setReturnInfo("扣款成功！！");
		
		Boolean isSucc =  offerTransactionService.executeOfferBack(objects);
		System.out.println(isSucc);
	}
	
	
}
