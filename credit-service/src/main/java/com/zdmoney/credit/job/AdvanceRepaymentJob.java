package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.dao.pub.ILoanAdvanceRepaymentDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 结清客户明细表提前结清
 * @author 00236632
 *
 */
@Service
public class AdvanceRepaymentJob {

	private static final Logger logger = Logger.getLogger(AdvanceRepaymentJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ILoanAdvanceRepaymentDao dao;

	public void advanceRepayment() {
		
    	String isAdvanceRepayment = sysParamDefineService.getSysParamValue("sysJob", "isAdvanceRepayment");
		if(!Const.isClosing.equals(isAdvanceRepayment)){
			logger.info("AdvanceRepaymentJob开始........");
			loanLogService.createLog("AdvanceRepaymentJob", "info", "AdvanceRepaymentJob开始........", "SYSTEM");
			try {
				dao.insertAdvanceRepaymentJob();
			} catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("AdvanceRepaymentJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}
			logger.info("AdvanceRepaymentJob结束........");
			loanLogService.createLog("AdvanceRepaymentJob", "info", "AdvanceRepaymentJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("AdvanceRepaymentJob", "info", "定时开关isAdvanceRepayment关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isAdvanceRepayment关闭，此次不执行");
		}
	}

}
