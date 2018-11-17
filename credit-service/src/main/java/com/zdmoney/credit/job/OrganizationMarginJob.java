package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 机构保证金汇总表
 * @author 00236695
 *
 */
@Service
@Transactional
public class OrganizationMarginJob {
	
	private static final Logger logger = Logger.getLogger(OrganizationMarginJob.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private ILoanLogService loanLogService;

	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;

	public void execute() {
		String isOrganizationMargin = sysParamDefineService.getSysParamValue("sysJob", "isOrganizationMargin");

		if (!Const.isClosing.equals(isOrganizationMargin)) {
			logger.info("OrganizationMarginJob开始........");
			loanLogService.createLog("OrganizationMarginJob", "info", "OrganizationMarginJob开始........", "SYSTEM");

			try {
				jobFreeSqlDao.organizationMarginJobDelete();
				jobFreeSqlDao.organizationMarginJobInsert();
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("OrganizationMarginJob", "info", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}

			logger.info("OrganizationMarginJob结束........");
			loanLogService.createLog("OrganizationMarginJob", "info", "OrganizationMarginJob结束........", "SYSTEM");
		} else {
			loanLogService.createLog("OrganizationMarginJob", "info", "定时开关isOrganizationMargin关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isOrganizationMargin关闭，此次不执行");
		}
	}
}
