
package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * 逾期日报 job
 * @author YM10112
 *
 */
@Service
public class OverdueDailyJob {
	private static final Logger logger = Logger.getLogger(OverdueDailyJob.class);
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	@Autowired
    private ILoanLogService loanLogService;
	
	public void overdueDailyExecute(){
		String isDayReportSnapshotJobInsert = sysParamDefineService.getSysParamValue("sysJob", "isDayReportSnapshotJobInsert");
		if(!Const.isClosing.equals(isDayReportSnapshotJobInsert)){
			logger.info("创建【逾期日报快照数据】开始....当前日期为："+DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			loanLogService.createLog("OverdueDailyJob", "info","创建【逾期日报快照数据】开始", "SYSTEM");
			jobFreeSqlDao.dayReportSnapshotJobInsert();
			loanLogService.createLog("OverdueDailyJob", "info","创建【逾期日报快照数据】结束", "SYSTEM");
			logger.info("创建【逾期日报快照数据】结束....当前日期为："+DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		}else{
			logger.info("创建【逾期日报】job开关已关闭....");
			loanLogService.createLog("OverdueDailyJob", "info","创建【逾期日报快照数据】开关已关闭", "SYSTEM");
		}
	}

}
