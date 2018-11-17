package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class JimuheziReturnRiskJob {
	
	private static final Logger logger = Logger.getLogger(JimuheziReturnRiskJob.class);
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private ILoanLogService loanLogService;
	public void execute() {
    	String isJimuheziReturnRisk = sysParamDefineService.getSysParamValue("sysJob", "isJimuheziReturnRisk");
		if(!Const.isClosing.equals(isJimuheziReturnRisk)){
			
			logger.info("积木盒子风险金退回处理开始........");
			loanLogService.createLog("JimuheziReturnRiskJob", "info", "积木盒子风险金退回处理开始........", "SYSTEM");
	        SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(new Date());
	        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));//获取月初时间
	        
	        String startDate = df.format(calendar.getTime());
	        String endDate = df.format(new Date());
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("startDate", startDate);
	        params.put("endDate", endDate);
	        
	        List<Map<String, Object>> results = jobFreeSqlDao.jimuReturnRiskData(params);
	        
	        for (Map<String, Object> result : results) {
	        	try {
	            	Long loanId = ((BigDecimal) result.get("ID")).longValue();
	            	int currentTerm =  ((BigDecimal)result.get("CURRENTTERM")).intValue();
	            	BigDecimal pactMoney = (BigDecimal)result.get("PACTMONEY");
	            	
	            	afterLoanService.returnJimuRisk(loanId, currentTerm, pactMoney);
	        	} catch (Exception e) {
	        		logger.error((Long) result.get("ID")+"&&"+e.getMessage());
					int length = e.getMessage().length();
	        		loanLogService.createLog("JimuheziReturnRiskJob", "error", (Long) result.get("ID")+"&&"+(length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
	        	}
	        }
	        logger.info("积木盒子风险金退回处理结束");
	        loanLogService.createLog("JimuheziReturnRiskJob", "info", "积木盒子风险金退回处理结束", "SYSTEM");
		}else{
			loanLogService.createLog("JimuheziReturnRiskJob", "info", "定时开关isJimuheziReturnRisk关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isJimuheziReturnRisk关闭，此次不执行");
		}
		

	}
}
