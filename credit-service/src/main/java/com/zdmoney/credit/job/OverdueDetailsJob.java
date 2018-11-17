package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class OverdueDetailsJob {

	private static final Logger logger = Logger.getLogger(OverdueDetailsJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private ILoanLogService loanLogService;
	
	
	public void execute() {
		
    	String isOverdueDetails = sysParamDefineService.getSysParamValue("sysJob", "isOverdueDetails");
		if(!Const.isClosing.equals(isOverdueDetails)){
			logger.info("OverdueDetailsJob开始........");
			loanLogService.createLog("OverdueDetailsJob", "info", "OverdueDetailsJob开始........", "SYSTEM");
			try {
				jobFreeSqlDao.overdueDetailsJobDelete();
				jobFreeSqlDao.overdueDetailsJobInsert();
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("OverdueDetailsJob", "info", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}

			logger.info("OverdueDetailsJob结束........");
			loanLogService.createLog("OverdueDetailsJob", "info", "OverdueDetailsJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("OverdueDetailsJob", "info", "定时开关isOverdueDetails关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isOverdueDetails关闭，此次不执行");
		}

	}
}
