
package com.zdmoney.credit.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentStateService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * 查询历史还款状态定时job
 * @author Charming
 *
 */
@Service
public class LoanRepaymentStateJob {
	private static final Logger logger = Logger.getLogger(LoanRepaymentStateJob.class);
	@Autowired
	ILoanRepaymentStateService loanRepaymentStateService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void loanRepaymentStateQueryJob(){
		String isLoanRepaymentState = sysParamDefineService.getSysParamValue("sysJob", "isLoanRepaymentState");//定时任务开关
		if(!Const.isClosing.equals(isLoanRepaymentState)){
			logger.info("☆☆☆☆☆☆☆☆☆☆LoanRepaymentStateJob历史还款状态定时job开始☆☆☆☆☆☆☆☆☆☆");
			Date currDate = Dates.getCurrDate();
			try {
				loanRepaymentStateService.createLoanRepaymentState(currDate);
				logger.info("☆☆☆☆☆☆☆☆☆☆LoanRepaymentStateJob历史还款状态定时job结束☆☆☆☆☆☆☆☆☆☆");
			} catch (Exception e) {
				logger.error("☆☆☆☆☆☆☆☆☆☆LoanRepaymentStateJob历史还款状态定时job结束☆☆☆☆☆☆☆☆☆☆",e);
			}
		}else{
			logger.info("☆☆☆☆☆☆☆☆☆☆LoanRepaymentStateJob历史还款状态定时job 未开启☆☆☆☆☆☆☆☆☆☆");
		}
	}

}
