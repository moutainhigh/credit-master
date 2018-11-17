package com.zdmoney.credit.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.person.vo.PersonVo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.tytx.TytxService;
import com.zdmoney.credit.tytx.domain.CreditSmsHistory;
import com.zdmoney.credit.tytx.pub.ICreditSmsHistoryService;

/**
 * 查询离职客户经理名下的客户信息进行短信通知任务job(每周一10点发送)
 * @author fuhongxing
 * @date 2016年6月15日
 * @veersion 1.0.0
 */
@Service
public class LeaveOfficeEmployeeJob {
	private static final Logger logger = Logger.getLogger(LeaveOfficeEmployeeJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	
	@Autowired
	private TytxService tytxService;
	
	@Autowired
	private ICreditSmsHistoryService iCreditSmsHistoryService;
	
	@Autowired
	private IPersonInfoService iPersonInfoService;
	
	public void doNoticeNormal() {
		logger.info("========开始执行离职客户经理名下的客户短信通知job定时任务========");
		
		//获取job是否开启(1开启，0不开启)
    	String isXintuoguominCustomerInfoMail = sysParamDefineService.getSysParamValue("sysJob", "leaveOfficeEmployeeSendMessage");
		if(!Const.isClosing.equals(isXintuoguominCustomerInfoMail)){
			try {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("status", Constants.TYTX_SUCCESS_CODE);
				paramMap.put("type", "leaveOfficeEmployeeSendMessage");
				List<CreditSmsHistory> smsList = null;
				//查询离职客户经理名下的客户信息
				List<PersonVo> list = iPersonInfoService.findLeaveOfficeEmployee(paramMap);
				for (PersonVo person : list) {
					String msgContent ="尊敬的客户您好！证大财富温馨提醒您：您的客户经理{name}已于{date}离职。如您有借款和还款方面的问题，请联系当地营业部或我司服务热线【400-821-6888】进行咨询";
					paramMap.put("mobile", person.getMobile());
					paramMap.put("employeeId", person.getEmployeeId());
					if(person.getEmployeeName() != null && person.getLeaveOfficeDate()!=null){
						msgContent = msgContent.replace("{name}", person.getEmployeeName()).replace("{date}", DateFormatUtils.format(person.getLeaveOfficeDate(), "yyyy年MM月dd日"));
						//查询是否已经发送过两次通知短信
						smsList = iCreditSmsHistoryService.findByMobile(paramMap);
						if(smsList == null || smsList.size() < 2){
							//发送短信
							paramMap.put("content", msgContent);
							tytxService.sendMsg(paramMap);
						}else{
							logger.info("当前用户已发送两次短信，本次不做发送处理");
						}
					}else{
						logger.warn("客户经理信息不完整");
					}
				}
				
			} catch (Exception e) {
				logger.error("离职客户经理名下客户短信发送job异常！！！", e);
			}
		}else{
			loanLogService.createLog("leaveOfficeEmployeeSendMessage", "info", "定时开关leaveOfficeEmployeeSendMessage关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关leaveOfficeEmployeeSendMessage关闭，此次不执行");
		}


	}
	
}
