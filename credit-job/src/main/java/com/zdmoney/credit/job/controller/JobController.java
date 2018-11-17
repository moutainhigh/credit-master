package com.zdmoney.credit.job.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.job.AdvanceRepaymentJob;
import com.zdmoney.credit.job.CTSBatchInterfaceJob;
import com.zdmoney.credit.job.CreateOfferJob;
import com.zdmoney.credit.job.EndOfMonthNotifyJob;
import com.zdmoney.credit.job.ExportLoanStatusDetailJob;
import com.zdmoney.credit.job.FtpUploadBorrowerPersonDataJob;
import com.zdmoney.credit.job.FuiouOfferJob;
import com.zdmoney.credit.job.JimuheziDrawRiskJob;
import com.zdmoney.credit.job.JimuheziFillRiskJob;
import com.zdmoney.credit.job.JimuheziPaymentRisk10AMJob;
import com.zdmoney.credit.job.JimuheziPaymentRisk3PMJob;
import com.zdmoney.credit.job.JimuheziPaymentRisk4PMJob;
import com.zdmoney.credit.job.JimuheziPaymentRisk6PMJob;
import com.zdmoney.credit.job.JimuheziReturnRiskJob;
import com.zdmoney.credit.job.LeaveOfficeEmployeeJob;
import com.zdmoney.credit.job.LoanDistributionJob;
import com.zdmoney.credit.job.LoanRepaymentStateJob;
import com.zdmoney.credit.job.LufaxBusinessJob;
import com.zdmoney.credit.job.OrganizationMarginJob;
import com.zdmoney.credit.job.OverdueDetailsJob;
import com.zdmoney.credit.job.OverdueRatioStatJob;
import com.zdmoney.credit.job.OverdueStatJob;
import com.zdmoney.credit.job.PaidInEntityJob;
import com.zdmoney.credit.job.RealtimeDeductJob;
import com.zdmoney.credit.job.RefundedOfMoneyConfirmationJob;
import com.zdmoney.credit.job.RepayRemindEMSJob;
import com.zdmoney.credit.job.RepayResultDisposeBsyhJob;
import com.zdmoney.credit.job.RepaymentLevelJob;
import com.zdmoney.credit.job.ResidualPactMoneyDetailJob;
import com.zdmoney.credit.job.SyncBlackListJob;
import com.zdmoney.credit.job.SyncBlackListZSJob;
import com.zdmoney.credit.job.TaskReminderJob;
import com.zdmoney.credit.job.UpdateOverdueHistoryJob;
import com.zdmoney.credit.job.XintuobohaiCustomerInfoMailJob;
import com.zdmoney.credit.job.XintuoguominCustomerInfoMailJob;
import com.zdmoney.credit.job.XintuojihuaCustomerInfo4PMJob;

/**
 * job调用Controller，可以手工出发job
 * @author 00232949
 *
 */
@Controller
@RequestMapping("/job")
public class JobController  extends BaseController  {
	
	@Autowired 
	private CreateOfferJob createOfferJob;
	
	@Autowired
	private RealtimeDeductJob realtimeDeductJob;
	
	@Autowired
	private TaskReminderJob taskReminderJob;
	
	@Autowired
	private SyncBlackListJob syncBlackListJob;
	
	@Autowired
	private OverdueStatJob overdueStatJob;
	
	@Autowired
	private OverdueDetailsJob overdueDetailsJob;
	
	@Autowired
	private OverdueRatioStatJob overdueRatioStatJob;
	
	@Autowired
	private LoanDistributionJob loanDistributionJob;
	
	@Autowired
	private AdvanceRepaymentJob advanceRepaymentJob;
	
	@Autowired
	private EndOfMonthNotifyJob endOfMonthNotifyJob;
	
	@Autowired
	private FuiouOfferJob fuiouOfferJob;
	
	@Autowired
	private SyncBlackListZSJob syncBlackListZSJob;
	
	@Autowired
	private XintuoguominCustomerInfoMailJob xintuoguominCustomerInfoMailJob;
	
	@Autowired
	private RepayRemindEMSJob repayRemindEMSJob;
	
	@Autowired
	private JimuheziDrawRiskJob jimuheziDrawRiskJob;
	
	@Autowired
	private JimuheziFillRiskJob jimuheziFillRiskJob;
	
	@Autowired
	private JimuheziReturnRiskJob jimuheziReturnRiskJob;
	
	@Autowired
	private JimuheziPaymentRisk10AMJob jimuheziPaymentRisk10AMJob;
	
	@Autowired
	private JimuheziPaymentRisk3PMJob jimuheziPaymentRisk3PMJob;
	
	@Autowired
	private JimuheziPaymentRisk4PMJob jimuheziPaymentRisk4PMJob;
	
	@Autowired
	private JimuheziPaymentRisk6PMJob jimuheziPaymentRisk6PMJob;
	
	@Autowired
	private XintuojihuaCustomerInfo4PMJob xintuojihuaCustomerInfo4PMJob;
	
	@Autowired
	private CTSBatchInterfaceJob ctsBatchInterfaceJob;
	
	@Autowired
	private ResidualPactMoneyDetailJob residualPactMoneyDetailJob;

	@Autowired
	private UpdateOverdueHistoryJob updateOverdueHistoryJob;

	@Autowired
	private OrganizationMarginJob organizationMarginJob;
	
	@Autowired
	private ExportLoanStatusDetailJob exportLoanStatusDetailJob;
	
	@Autowired
	private XintuobohaiCustomerInfoMailJob xintuobohaiCustomerInfoMailJob;
	@Autowired
	private LeaveOfficeEmployeeJob leaveOfficeEmployeeJob;
	@Autowired
	private FtpUploadBorrowerPersonDataJob ftpUploadBorrowerPersonDataJob;
	@Autowired
	private RepaymentLevelJob repaymentLevelJob;
	
	@Autowired
	private PaidInEntityJob paidInEntityJob;
	@Autowired
	private RefundedOfMoneyConfirmationJob refundedOfMoneyConfirmationJob;
	
	@Autowired
	private LufaxBusinessJob lufaxBusinessJob;
	@Autowired
	private RepayResultDisposeBsyhJob repayResultDisposeBsyhJob;
	@Autowired
	private LoanRepaymentStateJob loanRepaymentStateJob;
	
	@RequestMapping("/realtimeDeductJob")
	public void  realtimeDeductJob(HttpServletRequest request, HttpServletResponse response) {
		realtimeDeductJob.realtimeDeductExecute();
	}
	
	@RequestMapping("/createOfferJob")
	public void  createOfferJob(HttpServletRequest request, HttpServletResponse response) {
		createOfferJob.createOverdueOffer();
		createOfferJob.createRepaymentDateOffer();
		createOfferJob.createSpecialOffer();
		
	}
	
	@RequestMapping("/taskReminderJob")
	public void  taskReminderJob(HttpServletRequest request, HttpServletResponse response) {
		taskReminderJob.execute();
	}
	
	@RequestMapping("/syncBlackListJob")
	public void  syncBlackListJob(HttpServletRequest request, HttpServletResponse response) {
		syncBlackListJob.execute();
	}
	
	@RequestMapping("/overdueStatJob")
	public void  overdueStatJob(HttpServletRequest request, HttpServletResponse response) {
		overdueStatJob.execute();
	}
	
	@RequestMapping("/overdueDetailsJob")
	public void  overdueDetailsJob(HttpServletRequest request, HttpServletResponse response) {
		overdueDetailsJob.execute();
	}
	
	@RequestMapping("/overdueRatioStatJob")
	public void  overdueRatioStatJob(HttpServletRequest request, HttpServletResponse response) {
		overdueRatioStatJob.execute();
	}
	
//	@RequestMapping("/loanDistributionJob")
//	public void  loanDistributionJob(HttpServletRequest request, HttpServletResponse response) {
//		loanDistributionJob.execute();
//	}
	
	@RequestMapping("/advanceRepaymentJob")
	public void  advanceRepaymentJob(HttpServletRequest request, HttpServletResponse response) {
		advanceRepaymentJob.advanceRepayment();
	}
	
	@RequestMapping("/endOfMonthNotifyJob")
	public void  endOfMonthNotifyJob(HttpServletRequest request, HttpServletResponse response) {
		endOfMonthNotifyJob.execute();
	}
	
	@RequestMapping("/fuiouOfferJob")
	public void  fuiouOfferJob(HttpServletRequest request, HttpServletResponse response) {
		fuiouOfferJob.fuiouOffer();
	}
	
	@RequestMapping("/syncBlackListZSJob")
	public void  syncBlackListZSJob(HttpServletRequest request, HttpServletResponse response) {
		syncBlackListZSJob.execute();
	}
	
	@RequestMapping("/xintuoguominCustomerInfoMailJob")
	public void  xintuoguominCustomerInfoMailJob(HttpServletRequest request, HttpServletResponse response) {
		xintuoguominCustomerInfoMailJob.doNoticeNormal();
	}
	
	@RequestMapping("/repayRemindEMSJob")
	public void  repayRemindEMSJob(HttpServletRequest request, HttpServletResponse response) {
		repayRemindEMSJob.execute();
	}
	
	@RequestMapping("/jimuheziDrawRiskJob")
	public void  jimuheziDrawRiskJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziDrawRiskJob.execute();
	}
	
	@RequestMapping("/jimuheziFillRiskJob")
	public void  jimuheziFillRiskJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziFillRiskJob.execute();
	}
	
	@RequestMapping("/jimuheziReturnRiskJob")
	public void  jimuheziReturnRiskJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziReturnRiskJob.execute();
	}
	
	@RequestMapping("/jimuheziPaymentRisk10AMJob")
	public void  jimuheziPaymentRisk10AMJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziPaymentRisk10AMJob.execute();
	}
	
	@RequestMapping("/jimuheziPaymentRisk3PMJob")
	public void  jimuheziPaymentRisk3PMJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziPaymentRisk3PMJob.execute();
	}
	
	@RequestMapping("/jimuheziPaymentRisk4PMJob")
	public void  jimuheziPaymentRisk4PMJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziPaymentRisk4PMJob.execute();
	}
	
	@RequestMapping("/jimuheziPaymentRisk6PMJob")
	public void  jimuheziPaymentRisk6PMJob(HttpServletRequest request, HttpServletResponse response) {
		jimuheziPaymentRisk6PMJob.execute();
	}
	
	@RequestMapping("/xintuojihuaCustomerInfo4PMJob")
	public void  xintuojihuaCustomerInfo4PMJob(HttpServletRequest request, HttpServletResponse response) {
		xintuojihuaCustomerInfo4PMJob.doNoticeNormal();
		
	}
	
	@RequestMapping("/ctsBatchInterfaceJob")
	public void  ctsBatchInterfaceJob(HttpServletRequest request, HttpServletResponse response) {
		ctsBatchInterfaceJob.execute();
	}
	
	@RequestMapping("/residualPactMoneyDetailJob")
	public void ResidualPactMoneyDetailJob(HttpServletRequest request, HttpServletResponse response) {
		residualPactMoneyDetailJob.execute();		
	}
	
	@RequestMapping("/updateOverdueHistoryJob")
	public void updateOverdueHistoryJob(HttpServletRequest request, HttpServletResponse response) {
		updateOverdueHistoryJob.execute();
	}

	@RequestMapping("/organizationMarginJob")
	public void organizationMarginJob(HttpServletRequest request, HttpServletResponse response) {
		organizationMarginJob.execute();
	}
	
	@RequestMapping("/exportLoanStatusDetailJob")
	public void exportLoanStatusDetailJob(HttpServletRequest request, HttpServletResponse response) {
		exportLoanStatusDetailJob.execute();
	}
	
	/**渤海信托邮件发送请求*/
	@RequestMapping("/xintuobohaiCustomerInfoMailJob")
	public void xintuobohaiCustomerInfoMailJob(HttpServletRequest request, HttpServletResponse response) {
		xintuobohaiCustomerInfoMailJob.doNoticeNormal();
	}
	/**查询离职客户经理名下的客户定时短信发送请求*/
	@RequestMapping("/leaveOfficeEmployeeJob")
	public void leaveOfficeEmployeeJob(HttpServletRequest request, HttpServletResponse response) {
		leaveOfficeEmployeeJob.doNoticeNormal();
	}

	/**
	 * 渤海信托个人借款资料上传
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ftpUploadBorrowerPersonDataJob")
	public void ftpUploadBorrowerPersonDataJob(HttpServletRequest request, HttpServletResponse response){
		logger.info("开始执行个人借款信息上传JOB");
		String curryDate=request.getParameter("date");
		Map<String,Object> map=new HashMap<>();
		if(Strings.isEmpty(curryDate)){
			map.put("curryDate", Dates.getDateTime(new Date(), Dates.DEFAULT_DATE_FORMAT));
		}else{
			map.put("curryDate",Dates.parse(curryDate,Dates.DEFAULT_DATE_FORMAT));
		}
		ftpUploadBorrowerPersonDataJob.executeParam(map);
	}
	@RequestMapping("/executeLoandetailUploadBHFtp")
			 public void executeLoandetailUploadBHFtp(HttpServletRequest request, HttpServletResponse response){
		logger.info("开始执行放款明细上传job");
		String curryDateStr=request.getParameter("date");
		Date curryDate=null;
		if(Strings.isEmpty(curryDateStr)){
			curryDate=Dates.getNow();
		}else{
			curryDate=Dates.parse(curryDateStr,Dates.DEFAULT_DATE_FORMAT);
		}
		ftpUploadBorrowerPersonDataJob.executeLoandetailUploadBHFtpService(curryDate);
	}
	@RequestMapping("/executeLoandetailUploadBH2")
	public void executeLoandetailUploadBH2(HttpServletRequest request, HttpServletResponse response){
		logger.info("开始执行放款明细上传job");
		String curryDateStr=request.getParameter("date");
		Date curryDate=null;
		if(Strings.isEmpty(curryDateStr)){
			curryDate=Dates.getNow();
		}else{
			curryDate=Dates.parse(curryDateStr,Dates.DEFAULT_DATE_FORMAT);
		}
		ftpUploadBorrowerPersonDataJob.executeLoandetailUploadBHFtpService(curryDate);
	}
	
	@RequestMapping("/repaymentLevelJob")
	public void repaymentLevelJob(HttpServletRequest request, HttpServletResponse response) {
		repaymentLevelJob.execute();
	}
	@RequestMapping("/repayResultDisposeBsyhJob")
	public void repayResultDisposeBsyhJob(HttpServletRequest request, HttpServletResponse response) {
		repayResultDisposeBsyhJob.repayResultSearchBsyhExecute();
	}
	@RequestMapping(value="/paidInJob")
    public void  paidInEntityJob(HttpServletRequest request, HttpServletResponse response) {
        paidInEntityJob.execute();
    }

	public  void refundedOfMoneyConfirmationJob(HttpServletRequest request,HttpServletResponse response){
		String curryDateStr=request.getParameter("date");
		Date curryDate=null;
		if(Strings.isEmpty(curryDateStr)){
			curryDate=Dates.getNow();
		}else{
			curryDate=Dates.parse(curryDateStr,Dates.DEFAULT_DATE_FORMAT);
		}

		refundedOfMoneyConfirmationJob.excuteUploadRefundedOfMoneyConfirmationBookFile(curryDate);
	}
	
	/**
	 * 还款日后一天挂账金额自动分账
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/billRepayDeal")
    public void  billRepayDealJob(HttpServletRequest request, HttpServletResponse response) {
		lufaxBusinessJob.billRepayDeal();
    }
	
	@RequestMapping(value="/loanRepaymentStateJob")
    public void  loanRepaymentStateJob(HttpServletRequest request, HttpServletResponse response) {
		loanRepaymentStateJob.loanRepaymentStateQueryJob();
    }
}
