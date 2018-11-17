package com.zdmoney.credit.job;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.ems.service.pub.IEMSService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;

@Service
public class RepayRemindEMSJob {

	private static final Logger logger = Logger.getLogger(RepayRemindEMSJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IEMSService emsService;
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	
	public void execute() {
    	String isRepayRemindEMS = sysParamDefineService.getSysParamValue("sysJob", "isRepayRemindEMS");
		if(!Const.isClosing.equals(isRepayRemindEMS)){
			logger.info("进入RepayRemindEMSJob........");
			loanLogService.createLog("RepayRemindEMSJob", "info", "进入RepayRemindEMSJob........", "SYSTEM");
	        Date today = Dates.getCurrDate();
	        Date nextRepayDate = getNextRepayDate(today);
	        Date beforeThreeNextRepayDate = getBeforeThreeNextRepayDate(nextRepayDate);//下一个还款日提前3天
	        Date sendEMSDate = getSendEMSDate(beforeThreeNextRepayDate);
	        Date sendWhiteBlackDate = getSendWhiteBlackDate(beforeThreeNextRepayDate);
	        
	        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

			if (today.equals(sendWhiteBlackDate)) {
				if (hour == 9) {
					doSendWhiteList(nextRepayDate);
				} else {
					// do nothing;
				}
			}

			if (today.equals(sendEMSDate)) {
				if (hour == 9) {
					doGenerateDateAndSendSMS(nextRepayDate,beforeThreeNextRepayDate);
				} else if (hour == 14) {
					doUpdateResultAndResendFailed(nextRepayDate,beforeThreeNextRepayDate);
				} else if (hour == 16) {
					doUpdateResultAndSendEMaiL(nextRepayDate,beforeThreeNextRepayDate);
				}
			}
			logger.info("离开RepayRemindEMSJob........");
			loanLogService.createLog("RepayRemindEMSJob", "info", "离开RepayRemindEMSJob........", "SYSTEM");
		}else{
			loanLogService.createLog("RepayRemindEMSJob", "info", "定时开关isRepayRemindEMS关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isRepayRemindEMS关闭，此次不执行");
		}

	}
	
    private void doSendWhiteList(Date repayDate){
    	logger.info("开始发送还款提醒短信号码白名单........");
    	loanLogService.createLog("RepayRemindEMSJob", "info", "开始发送还款提醒短信号码白名单........", "SYSTEM");
        try {
			emsService.emailMPhone(repayDate);
		} catch (IOException e) {
			logger.error(e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("RepayRemindEMSJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
		}
        logger.info("完成发送还款提醒短信号码白名单........");
        loanLogService.createLog("RepayRemindEMSJob", "info", "完成发送还款提醒短信号码白名单........", "SYSTEM");
    }

    private void doGenerateDateAndSendSMS(Date repayDate,Date beforeThreeNextRepayDate){
    	logger.info("开始发送还款提醒短信（初次）........");
    	loanLogService.createLog("RepayRemindEMSJob", "info", "开始发送还款提醒短信（初次）........", "SYSTEM");
        emsService.generateData(repayDate, false);
        try {
			emsService.batchGroupSendUnsend(repayDate,beforeThreeNextRepayDate);
		} catch (IOException e) {
			logger.error(e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("RepayRemindEMSJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
		}
        logger.info("完成发送还款提醒短信（初次）........");
        loanLogService.createLog("RepayRemindEMSJob", "info", "完成发送还款提醒短信（初次）........", "SYSTEM");
    }

    private void doUpdateResultAndResendFailed(Date repayDate,Date beforeThreeNextRepayDate){
    	logger.info("开始查询发送结果，并对发送失败的短信进行再次发送........");
    	loanLogService.createLog("RepayRemindEMSJob", "info", "开始查询发送结果，并对发送失败的短信进行再次发送........", "SYSTEM");
        try {
			emsService.batchGroupUpdateResult(repayDate,beforeThreeNextRepayDate);
			emsService.batchGroupSendFailedRetry(repayDate,beforeThreeNextRepayDate);
		} catch (IOException e) {
			logger.error(e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("RepayRemindEMSJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
		}
        logger.info("完成查询发送结果，并对发送失败的短信进行再次发送........");
        loanLogService.createLog("RepayRemindEMSJob", "info", "完成查询发送结果，并对发送失败的短信进行再次发送........", "SYSTEM");
    }

    private void doUpdateResultAndSendEMaiL(Date repayDate,Date beforeThreeNextRepayDate){
    	logger.info("开始查询发送结果，并发送邮件通知........");
        loanLogService.createLog("RepayRemindEMSJob", "info", "开始查询发送结果，并发送邮件通知........", "SYSTEM");
        
        try {
        	emsService.batchGroupUpdateResult(repayDate,beforeThreeNextRepayDate);
			emsService.emailSendResult(repayDate,beforeThreeNextRepayDate);
		} catch (IOException e) {
			logger.error(e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("RepayRemindEMSJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
		}
        logger.info("完成查询发送结果，并发送邮件通知........");
        loanLogService.createLog("RepayRemindEMSJob", "info", "完成查询发送结果，并发送邮件通知........", "SYSTEM");	
    }
	
    private Date getSendEMSDate(Date repayDate){
    	Calendar rapayDayCal=Calendar.getInstance();
    	rapayDayCal.setTime(repayDate);
        if (rapayDayCal.get(Calendar.YEAR) == 2015 && rapayDayCal.get(Calendar.MONTH) == 1){//2015年2月
            return sysSpecialDayService.getBeforeWorkday(repayDate,5);
        } else {
//            return  sysSpecialDayService.getBeforeWorkday(repayDate,4);
        	 Calendar cal=Calendar.getInstance();
             cal.setTime(repayDate);
             cal.add(Calendar.DATE, -3);//发送日期在还款日期前3天（2015-12-10修改）
             //ToolUtils.getBeforeWorkday(repayDate,4)
             return cal.getTime();
        }

    }

    private Date getSendWhiteBlackDate(Date repayDate){
    	Calendar rapayDayCal=Calendar.getInstance();
    	rapayDayCal.setTime(repayDate);
        if (rapayDayCal.get(Calendar.YEAR) == 2015 && rapayDayCal.get(Calendar.MONTH) == 1){//2015年2月
            return sysSpecialDayService.getBeforeWorkday(repayDate,7);
        } else {
//            return sysSpecialDayService.getBeforeWorkday(repayDate, 6);
        	 Calendar cal=Calendar.getInstance();
             cal.setTime(repayDate);
             cal.add(Calendar.DATE, -5);//导出白名单时间为：系统在短信发送前2天（2015-12-10修改）
             //ToolUtils.getBeforeWorkday(repayDate, 6)
             return cal.getTime();
        }
    }
    
    public static Date getNextRepayDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        if (cal.get(Calendar.DAY_OF_MONTH) >= 16){
            cal.add(Calendar.MONTH,1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }else {
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }        
        return cal.getTime();
    }
    
    private Date getBeforeThreeNextRepayDate(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -3);//下一个还款日提前3天(2015-12-10修改)
        return cal.getTime();
    }
}
