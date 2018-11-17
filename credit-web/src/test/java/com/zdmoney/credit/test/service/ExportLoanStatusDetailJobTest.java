package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.job.ExportLoanStatusDetailJob;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"/spring/*.xml"})  
public class ExportLoanStatusDetailJobTest {

	
	@Autowired
	private ExportLoanStatusDetailJob exportLoanStatusDetailJob;
	
	@Test
	@Rollback(false)
	public void test(){  
		exportLoanStatusDetailJob.execute();
	}
}
