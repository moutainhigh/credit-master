package com.zdmoney.credit.test.service;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class VLoanInfoServiceTest {
	
	@Autowired 
	IVLoanInfoService vLoanInfoService;
	
	
	@Test
	public void testFindByLoanId () {
		
		
		System.out.println(vLoanInfoService.findByLoanId(3886604l));
	}
	
	@Test
	public void testGetTQJQLoanByDate () {
		
		ComOrganization org = new ComOrganization();
//		org.setCode("0104004400050");
		List<VLoanInfo> list = vLoanInfoService.getTQJQLoanByDate(org, Dates.getCurrDate());
		
		System.out.println(list.size());
	}
	
	@Test
	public void testGetYQHKLoanByOrg() {
		
		ComOrganization org = new ComOrganization();
//		org.setCode("0104004400050");
		
		List<VLoanInfo> list2 = vLoanInfoService.getYQHKLoanByOrg(org);
		
		
		System.out.println(list2.size());
	}
	
	@Test
	public void testGetZCHKLoanByOrg() {
		
		ComOrganization org = new ComOrganization();
//		org.setCode("0104004400050");
		
		List<VLoanInfo> list2 = vLoanInfoService.getZCHKLoanByOrg(org,  Dates.getCurrDate());
		
		
		System.out.println(list2.size());
	}
}
