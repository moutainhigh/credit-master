package com.zdmoney.credit.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class OverdueStatJob {

	private static final Logger logger = Logger.getLogger(OverdueStatJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;

	@Autowired
	private ILoanLogService loanLogService;
	
	public void execute() {
		
    	String isOverdueStat = sysParamDefineService.getSysParamValue("sysJob", "isOverdueStat");
		if(!Const.isClosing.equals(isOverdueStat)){
			logger.info("OverdueStatJob开始........");
			loanLogService.createLog("OverdueStatJob", "info", "OverdueStatJob开始........", "SYSTEM");
			
			String dateStr = Dates.getDateTime(new Date(), Dates.DEFAULT_DATE_FORMAT);
			try {
				jobFreeSqlDao.overdueStatJobDelete(dateStr);
				jobFreeSqlDao.overdueStatJobInsert();
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("OverdueStatJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}

			logger.info("OverdueStatJob结束........");
			loanLogService.createLog("OverdueStatJob", "info", "OverdueStatJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("OverdueStatJob", "info", "定时开关isOverdueStat关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isOverdueStat关闭，此次不执行");
		}

	}

}
