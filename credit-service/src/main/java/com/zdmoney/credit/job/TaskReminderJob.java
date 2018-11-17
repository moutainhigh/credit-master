package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.service.pub.IEndOfDayService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 日终处理job
 * @author 00232949
 *
 */
@Service
public class TaskReminderJob {
	
	private static Logger logger = Logger.getLogger(TaskReminderJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IEndOfDayService endOfDayService;
	
	public void  execute() {
    	String isTaskReminder = sysParamDefineService.getSysParamValue("sysJob", "isTaskReminder");
		if(!Const.isClosing.equals(isTaskReminder)){
	        // execute job
			logger.info("日终处理开始===============");
			loanLogService.createLog("TaskReminderJob", "info", "日终处理开始===============", "SYSTEM");
	        endOfDayService.timerProcess();
	        loanLogService.createLog("TaskReminderJob", "info", "日终处理结束===============", "SYSTEM");
	        logger.info("日终处理结束===============");
		}else{
			loanLogService.createLog("TaskReminderJob", "info", "定时开关isTaskReminder关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isTaskReminder关闭，此次不执行");
		}
		

    }
}
