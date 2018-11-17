package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.service.pub.ILoanBackService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 回购信息上传job
 * @author user
 *
 */
@Service
public class LoanBackUploadJob {

	private static final Logger logger = Logger.getLogger(LoanBackUploadJob.class);
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	@Qualifier("loanBackServiceImpl")
    private ILoanBackService loanBackServiceImpl;
	
	public void execute() {
		String isLoanBackUploadJob = sysParamDefineService.getSysParamValue("sysJob", "isLoanBackUploadJob");
		if(Const.isOpening.equals(isLoanBackUploadJob)){
			loanLogService.createLog("LoanBackUploadJob", "info", "定时开关isLoanBackUploadJob关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isLoanBackUploadJob关闭，此次不执行");
			return;
		}
		logger.info("LoanBackUploadJob开始........");
		loanLogService.createLog("LoanBackUploadJob", "info", "LoanBackUploadJob开始........", "SYSTEM");
		try {
			loanBackServiceImpl.uploadLoanBack();
		    }catch (Exception e) {
				logger.error(e.getMessage());
				int length = e.getMessage().length();
				loanLogService.createLog("LoanBackUploadJob", "info", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
			}
			logger.info("LoanBackUploadJob结束........");
			loanLogService.createLog("LoanBackUploadJob", "info", "LoanBackUploadJob结束........", "SYSTEM");
	}}
