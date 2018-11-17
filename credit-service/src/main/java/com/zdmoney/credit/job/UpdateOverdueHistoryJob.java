package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class UpdateOverdueHistoryJob {

	private static final Logger logger = Logger.getLogger(OverdueDetailsJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private ILoanLogService loanLogService;
	public void execute() {
    	String isUpdateOverdueHistory = sysParamDefineService.getSysParamValue("sysJob", "isUpdateOverdueHistory");
		if(!Const.isClosing.equals(isUpdateOverdueHistory)){
			logger.info("UpdateOverudeHistoryJob开始........");
			loanLogService.createLog("UpdateOverudeHistoryJob", "info", "UpdateOverudeHistoryJob开始........", "SYSTEM");
			try {
				String dateStr = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT);
				jobFreeSqlDao.updateOverdueHistory(dateStr);
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("UpdateOverudeHistoryJob", "info", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}

			logger.info("UpdateOverudeHistoryJob结束........");
			loanLogService.createLog("UpdateOverudeHistoryJob", "info", "UpdateOverudeHistoryJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("UpdateOverudeHistoryJob", "info", "定时开关isUpdateOverdueHistory关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isUpdateOverdueHistory关闭，此次不执行");
		}
	}

}
