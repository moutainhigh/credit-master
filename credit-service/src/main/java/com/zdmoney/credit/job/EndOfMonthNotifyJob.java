package com.zdmoney.credit.job;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class EndOfMonthNotifyJob {
	private static final Logger logger = Logger.getLogger(EndOfMonthNotifyJob.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private ILoanLogService loanLogService;
	
	
	public void execute() {
		logger.info("EndOfMonthNotifyJob开始........");
		loanLogService.createLog("EndOfMonthNotifyJob", "info", "EndOfMonthNotifyJob开始........", "SYSTEM");
    	String isEndOfMonthNotify = sysParamDefineService.getSysParamValue("sysJob", "isEndOfMonthNotify");
		if(!Const.isClosing.equals(isEndOfMonthNotify)){
			Calendar c = Calendar.getInstance();
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			if (1== day || 16 == day) {
				boolean endOfMonthOpened = "t".equals(sysParamDefineService.getSysParamValue("codeHelper", "end_of_month_opened"));
				
				if (endOfMonthOpened) {
					sysParamDefineService.updateSysParamValue("codeHelper", "end_of_month_opened", "f");
				}
			}
		}else{
			loanLogService.createLog("EndOfMonthNotifyJob", "info", "定时开关isEndOfMonthNotify关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isEndOfMonthNotify关闭，此次不执行");
		}
		
		logger.info("EndOfMonthNotifyJob结束........");
		loanLogService.createLog("EndOfMonthNotifyJob", "info", "EndOfMonthNotifyJob结束........", "SYSTEM");

	}

}
