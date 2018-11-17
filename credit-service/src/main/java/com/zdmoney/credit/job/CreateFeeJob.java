
package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * 第一次服务费划扣失败，再次划扣服务费job
 * @author YM10104
 *
 */
@Service
public class CreateFeeJob {
	private static final Logger logger = Logger.getLogger(CreateFeeJob.class);
	@Autowired
	LoanFeeUtil loanFeeUtil;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void createFeeQuery(){
		String isTppGrantResultQueryStat = sysParamDefineService.getSysParamValue("sysJob", "isCreateFeeJobStat");
		if(!Const.isClosing.equals(isTppGrantResultQueryStat)){
			logger.info("☆☆☆☆☆☆☆☆☆☆CreateFeeJob划扣服务费开始☆☆☆☆☆☆☆☆☆☆");
			
			loanFeeUtil.createFeeSecond();
			
			logger.info("☆☆☆☆☆☆☆☆☆☆CreateFeeJob划扣服务费结束☆☆☆☆☆☆☆☆☆☆");
		}else{
			logger.info("☆☆☆☆☆☆☆☆☆☆CreateFeeJob划扣服务费  未开启☆☆☆☆☆☆☆☆☆☆");
		}
	}

}
