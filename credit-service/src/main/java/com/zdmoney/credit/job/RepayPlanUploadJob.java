package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;


/**
 * 外贸3还款计划上传job
 * @author YM10112
 *
 */
 
@Service
public class RepayPlanUploadJob {
	private static final Logger logger = Logger.getLogger(RepayPlanUploadJob.class);
	@Autowired
	private ILoanRepaymentDetailService loanRepaymentDetailService;
	
	public void repayPlanUploadExecute() {
		logger.info("【外贸3】还款计划上传job开始执行...");
		loanRepaymentDetailService.executeRepayPlanUpload();
	}
}

