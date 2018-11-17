package com.zdmoney.credit.test.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  

@Transactional
public class LoanRepaymentDetailServiceTest {
	@Autowired 
	ILoanRepaymentDetailService loanRepaymentDetailService;
	
	
	@Test
	public void testFindByAccountForUpdate () {
		 Map<String, Object> repaymentMap = new HashMap<String, Object>();
			repaymentMap.put("loanId", 700290l);
			repaymentMap.put("currDate", Calendar.getInstance().getTime());
//			lastDrawRiskDate=Dates.parse("2015-11-01", "yyyy-MM-dd");
			repaymentMap.put("minDate", Dates.parse("2015-11-01", "yyyy-MM-dd"));
			repaymentMap.put("state", RepaymentStateEnum.结清.toString());
		
		BigDecimal d = loanRepaymentDetailService.getDrawRiskSumDeficit(repaymentMap);
		System.out.println("======================="+d);
	}
}
