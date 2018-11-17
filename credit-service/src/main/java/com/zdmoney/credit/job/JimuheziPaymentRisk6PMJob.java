package com.zdmoney.credit.job;

import java.text.SimpleDateFormat;
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
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;
@Service
public class JimuheziPaymentRisk6PMJob {
	
	private static final Logger logger = Logger.getLogger(JimuheziPaymentRisk6PMJob.class);

	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private ISendMailService sendMailService;
    private SimpleDateFormat format =new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00");
//    private SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd 00:00");
    private SimpleDateFormat df3 =new SimpleDateFormat("yyyy-MM-dd");
	
	public void execute() {
    	String isJimuheziPaymentRisk6PM = sysParamDefineService.getSysParamValue("sysJob", "isJimuheziPaymentRisk6PM");
		if(!Const.isClosing.equals(isJimuheziPaymentRisk6PM)){
			Date today = Dates.getCurrDate();

			if (sysSpecialDayService.isWorkDay(today) && ToolUtils.isRepayDay(today)) {

				doFillRiskBatchJimuBatch(today);
			}
		}else{
			loanLogService.createLog("JimuheziPaymentRisk6PMJob", "info", "定时开关isJimuheziPaymentRisk6PM关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isJimuheziPaymentRisk6PM关闭，此次不执行");
		}
		

	}
	

	
	private void doFillRiskBatchJimuBatch(Date date) {
		try {
            List<JimuData> datas = new ArrayList<JimuData>();

            Calendar calendar = Calendar.getInstance();//获得日历对象
            calendar.setTime(date);//设置时间为传递过来的时间
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, -3);    //得到前三个小时
            calendar.add(Calendar.DATE, 1);    //得到后一天
            calendar.add(Calendar.MONTH, -1);    //得到前一个月

            String lastTime=df.format(cal.getTime());
            String currentTime=df.format(new Date());
            String startDate = df3.format(calendar.getTime());
            String endDate = df3.format(date);
            
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("day", day);
            param.put("lastTime", lastTime);
            param.put("currentTime", currentTime);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            
            datas = jobFreeSqlDao.jimuNotifyData(param);
            
            for (JimuData data : datas) {
            	try {
            		afterLoanService.generateFlowForRepayAllJimuPay(data.getLoanId(), data.getCurrentTerm(), Dates.getCurrDate());
            	} catch (Exception e) {
            		logger.error(data.getLoanId()+"&&"+data.getCurrentTerm()+"&&"+e.getMessage());
    				int length = e.getMessage().length();
            		loanLogService.createLog("JimuheziPaymentRisk6PMJob", "error", data.getLoanId()+"&&"+data.getCurrentTerm()+"&&"+(length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
            	}
            }
            
            if (datas.size() > 0) {
                String now = format.format(new Date());
                String title = now+"积木盒子提前结清通知";
                StringBuffer stringBuffer= new StringBuffer();
                stringBuffer.append(now+"，积木盒子债权提前结清的名单，见附件。");
                logger.info("积木提前结清通知发送邮件处理开始........");
                loanLogService.createLog("JimuheziPaymentRisk6PMJob", "info", "积木提前结清通知发送邮件处理开始........", "SYSTEM");
                sendMailService.sendPaymentRiskAttachMail(
                		sysParamDefineService.getSysParamValue("jimu", "jimu.notify.to"), 
    					sysParamDefineService.getSysParamValue("jimu", "jimu.notify.cc"),
                		title, stringBuffer.toString(),datas);
                logger.info("积木提前通知发送邮件结束");
                loanLogService.createLog("JimuheziPaymentRisk6PMJob", "info", "积木提前通知发送邮件结束", "SYSTEM");
            }
		} catch (Exception e) {
			logger.error("jimuhezi提前结清通知，数据附件处理出错:" + e.getMessage());
			int length = e.getMessage().length();
			loanLogService.createLog("JimuheziPaymentRisk6PMJob", "error", "jimuhezi提前结清通知，数据附件处理出错:" + (length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
		}
	}
}
