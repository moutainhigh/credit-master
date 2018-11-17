package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.core.service.pub.IExportCoreService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class CTSBatchInterfaceJob {
	private static final Logger logger = Logger.getLogger(AdvanceRepaymentJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private IExportCoreService exportCoreService;
	@Autowired
	private ILoanLogService loanLogService;
	
	public void execute() {
    	String isCTSBatchInterface = sysParamDefineService.getSysParamValue("sysJob", "isCTSBatchInterface");
		if(!Const.isClosing.equals(isCTSBatchInterface)){
			logger.info("CTSBatchInterfaceJob开始........");
			loanLogService.createLog("CTSBatchInterfaceJob", "info", "CTSBatchInterfaceJob开始........", "SYSTEM");
			try {
		        //全量导出
		        exportCoreService.exportBorrowerCSV();
		        exportCoreService.exportAddressCSV();
		        exportCoreService.exportContactCSV();
		        exportCoreService.exportRepaymentDetialCSV();
		        exportCoreService.exportTelCSV();

		        //每天全量导出
		        exportCoreService.exportPersonTotalCSV();
		        exportCoreService.exportLoanCSV();
		        exportCoreService.exportDepartmentCSV();

		        //全量、增量导出
		        exportCoreService.exportFlowCSV();
		        exportCoreService.exportRepayInfoCSV();

		        //导出后做记录
		        exportCoreService.updateIsSendForLoan();

		        //发送邮件
		        exportCoreService.sendEmail();
			} catch (Exception e) {
				logger.error("CTSBatchInterfaceJob-execute:"+e);
				int length = e.getMessage().length();
				loanLogService.createLog("CTSBatchInterfaceJob", "error", length > 2000 ? e.getMessage().substring(0, 1500) : e.getMessage(), "SYSTEM");
			}
			logger.info("CTSBatchInterfaceJob结束........");
			loanLogService.createLog("CTSBatchInterfaceJob", "info", "CTSBatchInterfaceJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("CTSBatchInterfaceJob", "info", "定时开关isCTSBatchInterface关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isCTSBatchInterface关闭，此次不执行");
		}
		

	}
		

}
