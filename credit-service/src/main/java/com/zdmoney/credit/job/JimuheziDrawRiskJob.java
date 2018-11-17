package com.zdmoney.credit.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class JimuheziDrawRiskJob {
	private static final Logger logger = Logger.getLogger(JimuheziDrawRiskJob.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private ILoanLogService loanLogService;
	public void execute() {
		drawRiskBatchJimu(Dates.getCurrDate());
	}
	
	private void drawRiskBatchJimu(Date tradeDate) {
		logger.info("1或16日积木风险金垫付处理开始........");
		loanLogService.createLog("JimuheziDrawRiskJob", "info", "1或16日积木风险金垫付处理开始........", "SYSTEM");
    	String isJimuheziDrawRisk = sysParamDefineService.getSysParamValue("sysJob", "isJimuheziDrawRisk");
		if(!Const.isClosing.equals(isJimuheziDrawRisk)){
			Calendar c = Dates.toCalendar(tradeDate);
			int promiseReturnDate = c.get(Calendar.DAY_OF_MONTH);
			
			List<Integer> promiseReturnDateList = new ArrayList<Integer>();
			int[] dateArray = Const.PROMISERETURNDATE;
			for (int i = 0; i < dateArray.length; i++) {
				promiseReturnDateList.add(dateArray[i]);
			}
			
			if (promiseReturnDateList.contains(promiseReturnDate)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("fundsSource", "积木盒子");
				param.put("promiseReturnDate", promiseReturnDate);
				List<String> stateList = new ArrayList<String>();
				stateList.add(LoanStateEnum.正常.toString());
				stateList.add(LoanStateEnum.逾期.toString());
				param.put("states", stateList);
				
				List<Long> ids = jobFreeSqlDao.getDrawJimuRiskData(param);
				
				for (Long id : ids) {
					try {
						afterLoanService.drawJimuRisk(id, tradeDate);
					} catch (Exception e) {
						logger.error(e.getMessage());
						int length = e.getMessage().length();
						loanLogService.createLog("JimuheziDrawRiskJob", "error", id + "&&" + (length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage()), "SYSTEM");
					}
				}
			}
		}else{
			loanLogService.createLog("JimuheziDrawRiskJob", "info", "定时开关isJimuheziDrawRisk关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isJimuheziDrawRisk关闭，此次不执行");
		}
		logger.info("JimuheziDrawRiskJob结束........");

		loanLogService.createLog("JimuheziDrawRiskJob", "info", "1或16日积木风险金垫付处理结束", "SYSTEM");
	}

}
