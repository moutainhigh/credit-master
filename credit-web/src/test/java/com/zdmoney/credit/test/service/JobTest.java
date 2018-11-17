package com.zdmoney.credit.test.service;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.job.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
	SyncBlackListZSJob syncBlackListZSJob;

	@Autowired
	UploadDeductResultJob uploadDeductResultJob;

	@Autowired
	FtpUploadLoanData2AbsJob ftpUploadLoanData2AbsJob;
	@Autowired
	FtpUploadBorrowerPersonDataJob ftpUploadBorrowerPersonDataJob;
	@Autowired
	FtpUploadLoanData2BHXTJob ftpUploadLoanData2BHXTJob;
	@Autowired
	RefundedOfMoneyConfirmationJob refundedOfMoneyConfirmationJob;
	
	@Autowired
	private LufaxBusinessJob lufaxBusinessJob;

	@Autowired
	private FlowNodeEmployeeManagerJob flowNodeEmployeeManagerJob;

	@Test
	@Rollback(false)
	public void testoffercreate() {
//		createOfferJob.createOverdueOffer();
//		createOfferJob.createRepaymentDateOffer();
//		createOfferJob.createSpecialOffer();
	}

	@Test
	@Rollback(false)
	public void testRealtimeDeductJob () {
//		realtimeDeductJob.realtimeDeductExecute();
	}

	public static void main(String[] args) throws IOException {
		JobTest j=new JobTest();
		j.getProperties();

	}
	 private void getProperties() throws IOException {  
         InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("zdsys.properties");  
         System.out.println("begin!!!");  
        Properties properties = new Properties();  
        try{  
             properties.load(inputStream);  
          }catch (IOException ioE){  
             ioE.printStackTrace();  
        }finally{  
              inputStream.close();  
          }  
         System.out.println("sftp.download.host:"+properties.getProperty("sftp.download.host"));  
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
	public void syncBlackListZSJob () {
		syncBlackListZSJob.execute();
	}

	@Test
	public void executeOneTimeSettlementAdvice(){
		uploadDeductResultJob.executeOneTimeSettlementAdvice();
	}

	@Test
	public  void excuteElectronicSignatureAbs(){
		ftpUploadLoanData2AbsJob.excuteElectronicSignatureAbs();
	}
	
	@Test
	@Rollback(true)
	public void exeBillRepayDeal () {
		lufaxBusinessJob.billRepayDeal();
	}
	@Test
	public void executeLoandetailUploadBHFtp(){
		ftpUploadBorrowerPersonDataJob.executeLoandetailUploadBHFtpService(Dates.getDateByString("2017-04-07",Dates.DEFAULT_DATE_FORMAT));
	}
	@Test
	public  void executeRepayPlanUploadBH(){
		ftpUploadBorrowerPersonDataJob.executeRepayPlanUploadBHService(Dates.getDateByString("2017-04-07", Dates.DEFAULT_DATE_FORMAT));
	}
	@Test
	public void executeBorrowerPersonDate(){
		ftpUploadBorrowerPersonDataJob.execute();
	}
	@Test
	public void ftpUploadLoanData2BHXTJob(){
		ftpUploadLoanData2BHXTJob.excute();
	}

	@Test
	public void excuteRefundedOfMoneyConfirmationJob(){
//		refundedOfMoneyConfirmationJob.excuteUploadRefundedOfMoneyConfirmationBookFile();
		refundedOfMoneyConfirmationJob.excuteUploadRefundedOfMoneyConfirmationBookFile(Dates.getDateByString("2017-04-07",Dates.DEFAULT_DATE_FORMAT));
	}

	@Test
	public void flowNodeEmployeeManagerJob(){
		flowNodeEmployeeManagerJob.execute();
	}
}
