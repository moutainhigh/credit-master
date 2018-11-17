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
public class ResidualPactMoneyDetailJob {
	
	private static final Logger logger = Logger.getLogger(ResidualPactMoneyDetailJob.class);
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	public void execute() {
		
		String isResidualPactMoneyDetail = sysParamDefineService.getSysParamValue("sysJob", "isResidualPactMoneyDetail");
		if(!Const.isClosing.equals(isResidualPactMoneyDetail)){
			logger.info("ResidualPactMoneyDetailJob开始........");
			loanLogService.createLog("ResidualPactMoneyDetailJob", "info", "ResidualPactMoneyDetailJob开始........", "SYSTEM");
			try {
				Date date = Dates.getCurrDate();
				jobFreeSqlDao.residualPactMoneyDetailJobDelete(date);
				jobFreeSqlDao.residualPactMoneyDetailJobInsert();				
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("ResidualPactMoneyDetailJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}
			logger.info("ResidualPactMoneyDetailJob结束........");
			loanLogService.createLog("ResidualPactMoneyDetailJob", "info", "ResidualPactMoneyDetailJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("ResidualPactMoneyDetailJob", "info", "定时开关isResidualPactMoneyDetail关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isResidualPactMoneyDetail关闭，此次不执行");
		}
	}

}
