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
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.exceldata.XintuoData;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.xingtuo.dao.pub.IXintuoDataDao;
import com.zdmoney.credit.xintuo.domain.XintuoDataDomain;

@Service
public class XintuojihuaCustomerInfo4PMJob {
	private static final Logger logger = Logger.getLogger(XintuojihuaCustomerInfo4PMJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IXintuoDataDao dao;
	@Autowired
	private ISendMailService sendMailService;
		
	public void doNoticeNormal() {
    	String isXintuojihuaCustomerInfo4PM = sysParamDefineService.getSysParamValue("sysJob", "isXintuojihuaCustomerInfo4PM");
		if(!Const.isClosing.equals(isXintuojihuaCustomerInfo4PM)){
			logger.info("XintuojihuaCustomerInfo4PMJob开始........");
			loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "XintuojihuaCustomerInfo4PMJob开始........", "SYSTEM");
			try {
				Date date = new Date();
				
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, -1);
				
				String startDate = Dates.getDateTime(c.getTime(), Dates.DEFAULT_DATETIME_FORMAT);
				String endTime = Dates.getDateTime(date, Dates.DEFAULT_DATETIME_FORMAT);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("startDate", startDate);
				params.put("endTime", endTime);
				
				List<XintuoDataDomain> datas = dao.getXintuoDataJob(params);
				
				List<XintuoData> mailDatas = new ArrayList<XintuoData>();
				
				for (XintuoDataDomain data : datas) {
					XintuoData mailData = data.toXintuoData();
					mailDatas.add(mailData);
				}
				
				String now = Dates.getDateTime(new Date(), "yyyy年MM月dd日 HH:mm");
				String title = now + "华澳信托白名单客户信息";
		        StringBuffer stringBuffer= new StringBuffer();
		        stringBuffer.append("  附件为"+now+"，华澳信托白名单客户信息，请查收，谢谢！");
				
				if (mailDatas.size() > 0) {
					loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "华澳信托白名单客户信息通知发送邮件处理开始........", "SYSTEM");
					sendMailService.sendXintuoCustomerAttachMail(sysParamDefineService.getSysParamValueCache("xintuo","xintuo.notify.to"), 
							sysParamDefineService.getSysParamValueCache("xintuo","xintuo.notify.cc"), title, stringBuffer.toString(), mailDatas);
					loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "华澳信托白名单客户信息通知发送邮件结束", "SYSTEM");
				} else {
					loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "无数据导出....", "SYSTEM");
				}
			} catch (Exception e){
	            e.printStackTrace();
	        }
			logger.info("XintuojihuaCustomerInfo4PMJob結束........");
			loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "XintuojihuaCustomerInfo4PMJob結束........", "SYSTEM");
		}else{
			loanLogService.createLog("XintuojihuaCustomerInfo4PMJob", "info", "定时开关isXintuojihuaCustomerInfo4PM关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isXintuojihuaCustomerInfo4PM关闭，此次不执行");
		}

	}

}
