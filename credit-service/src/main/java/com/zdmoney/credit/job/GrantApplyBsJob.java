
package com.zdmoney.credit.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * @author YM10112  2016年12月28日
 *
 */
@Service
public class GrantApplyBsJob {
	private static final Logger logger = Logger.getLogger(GrantApplyBsJob.class);
	@Value("${gateway.interface.url}")
    public String gatewayInterfaceUrl;
	@Autowired
	private IFinanceGrantService financeGrantService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
    private ISysParamDefineService sysParamDefineService;
	public void grantApplyBsExecute(){
		logger.info("开始执行 财务放款申请JOB---");
		loanLogService.createLog("grantApplyBsExecute", "info","开始执行 财务放款申请JOB---", "SYSTEM");
		String isApplyFinanceGrant = sysParamDefineService.getSysParamValue("sysJob", "isApplyFinanceGrant");
		if (Const.isClosing.equals(isApplyFinanceGrant)) {
			logger.warn("定时开关isApplyFinanceGrant关闭，此次不执行");
			loanLogService.createLog("grantApplyBsExecute", "info","定时开关isApplyFinanceGrant关闭，此次不执行", "SYSTEM");
			return;
		}
		List<LoanBaseGrantVo> loanBaseGrantVoList = financeGrantService.searchFinaceGrantApplyDetail("bs");
		if (CollectionUtils.isEmpty(loanBaseGrantVoList)) {
			logger.info("没有需要申请财务放款的的债权!结束grantApplyBsExecute JOB");
			loanLogService.createLog("grantApplyBsExecute", "info","没有需要申请财务放款的的债权!结束grantApplyBsExecute JOB", "SYSTEM");
			return;
		}
		financeGrantService.batchRequestFinanceGrantApply(loanBaseGrantVoList);
		logger.info("执行executeFinanceGrantApply JOB结束");
		loanLogService.createLog("grantApplyBsExecute", "info","执行grantApplyBsExecute JOB结束", "SYSTEM");
	}
	
	public void grantApplyWm3Execute(){
		logger.info("开始执行 外贸3财务放款申请JOB---");
		loanLogService.createLog("grantApplyWm3Execute", "info","开始执行 外贸3财务放款申请JOB---", "SYSTEM");
		String isApplyFinanceGrant = sysParamDefineService.getSysParamValue("sysJob", "isApplyFinanceGrant");
		if (Const.isClosing.equals(isApplyFinanceGrant)) {
			logger.warn("定时开关isApplyFinanceGrant关闭，此次不执行");
			loanLogService.createLog("grantApplyWm3Execute", "info","定时开关isApplyFinanceGrant关闭，此次不执行", "SYSTEM");
			return;
		}
		List<LoanBaseGrantVo> loanBaseGrantVoList = financeGrantService.searchFinaceGrantApplyDetail("wm3");
		if (CollectionUtils.isEmpty(loanBaseGrantVoList)) {
			logger.info("没有需要申请外贸3财务放款的的债权!结束grantApplyBsExecute JOB");
			loanLogService.createLog("grantApplyWm3Execute", "info","没有需要申请财务放款的的债权!结束grantApplyWm3Execute JOB", "SYSTEM");
			return;
		}
		financeGrantService.batchRequestFinanceGrantApply(loanBaseGrantVoList);
		logger.info("执行executeFinanceGrantApply 外贸3财务放款申请JOB结束");
		loanLogService.createLog("grantApplyWm3Execute", "info","执行grantApplyWm3Execute JOB结束", "SYSTEM");		
	}
}
