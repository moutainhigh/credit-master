package com.zdmoney.credit.test.service;

import com.zdmoney.credit.job.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class JobTest {
	@Autowired 
	CreateOfferJob createOfferJob;
	
	@Autowired
	RealtimeDeductJob realtimeDeductJob;
	
	@Autowired
	TaskReminderJob taskReminderJob;
	
	@Autowired
	com.zdmoney.credit.job.SyncBlackListJob syncBlackListJob;
	
	@Autowired
	LoanBackUploadJob loanBackUploadJob;
	
	@Autowired
	ApplyBsyhDeductJob applyBsyhDeductJob;

	@Autowired
	TransmitLoanData2LufaxJob transmitLoanData2LufaxJob;
	@Test
	@Rollback(false)
	public void testoffercreate() {
		transmitLoanData2LufaxJob.execute();
//		createOfferJob.createOverdueOffer();
//		createOfferJob.createRepaymentDateOffer();
//		createOfferJob.createSpecialOffer();
	}
	
	
	@Test
	@Rollback(false)
	public void testRealtimeDeductJob () {
		realtimeDeductJob.realtimeDeductExecute();
	}
	
	@Test
	@Rollback(false)
	public void testTaskReminderJob () {
//		taskReminderJob.execute();
	}
	
	@Test
	@Rollback(false)
	public void SyncBlackListJob () {
//		syncBlackListJob.execute();
	}
	@Test
	@Rollback(false)
	public void testLoanBackUploadJob(){
		loanBackUploadJob.execute();
	}
	@Test
	@Rollback(false)
	public void testApplyBsyhDeductJob () {
		applyBsyhDeductJob.applyBsyhDeduct();
	}
}
