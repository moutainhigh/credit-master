
package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * 大额挂账金额提醒
 * @author Charming
 *
 */
@Service
public class BigAmountRemindJob {
	private static final Logger logger = Logger.getLogger(BigAmountRemindJob.class);
	@Autowired
	ILoanLedgerService loanLedgerService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void dealBigAmount(){
		String isBigAmountRemindJobStat = sysParamDefineService.getSysParamValue("sysJob", "isBigAmountRemindJobStat");
		if(!Const.isClosing.equals(isBigAmountRemindJobStat)){
			logger.info("☆☆☆☆☆☆☆☆☆☆大额挂账金额提醒开始☆☆☆☆☆☆☆☆☆☆");
			
			loanLedgerService.dealBigAmount();
			
			logger.info("☆☆☆☆☆☆☆☆☆☆大额挂账金额提醒结束☆☆☆☆☆☆☆☆☆☆");
		}else{
			logger.info("☆☆☆☆☆☆☆☆☆☆大额挂账金额提醒  未开启☆☆☆☆☆☆☆☆☆☆");
		}
	}

}
