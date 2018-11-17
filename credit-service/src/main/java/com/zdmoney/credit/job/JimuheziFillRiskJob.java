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
import com.zdmoney.credit.common.constant.MessageStateEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;

@Service
public class JimuheziFillRiskJob {
	private static final Logger logger = Logger.getLogger(JimuheziFillRiskJob.class);

	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private IBaseMessageService baseMessageService;
	
	@Autowired
	private IComEmployeeService employeeService;
	
	@Autowired
	private ISysDictionaryService sysDictionaryService;
	
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	
	@Autowired
	private ILoanLogService loanLogService;
	public void execute() {
    	String isJimuheziFillRisk = sysParamDefineService.getSysParamValue("sysJob", "isJimuheziFillRisk");
		if(!Const.isClosing.equals(isJimuheziFillRisk)){
	        //上一还款日的日期
	        Date today = Dates.getCurrDate();
	        Date lastRepayDate = ToolUtils.getLastRepayDate(today);

	        //如果使用工作日的逻辑，使用t+6的逻辑；如果使用工作日的逻辑，需要额外考虑T+10是否在下一还款日之后。
	        Date t10Date = sysSpecialDayService.getWorkday(lastRepayDate, 10);
//	        Date t6Date = ToolUtils.getWorkday(lastRepayDate, 6);
	        Date nextRepayDate = ToolUtils.getNextRepayDate(lastRepayDate);
	        Date beforeNextRepayDate1workday = sysSpecialDayService.getBeforeWorkday(nextRepayDate, 1);
//	        Date beforeNextRepayDate5workday = ToolUtils.getBeforeWorkday(nextRepayDate, 5);
//	        Date generatDataDay = t6Date ;
	        Date doFillDate = t10Date;
	        if(t10Date.compareTo(nextRepayDate)>= 0){
//	            generatDataDay = beforeNextRepayDate5workday;
	            doFillDate = beforeNextRepayDate1workday;
	        }

	        if(doFillDate.compareTo(today) == 0){
	            doFillRiskBatchJimuBatch(lastRepayDate,doFillDate);
	            passMessage(lastRepayDate, doFillDate);
	        }
		}else{
			loanLogService.createLog("JimuheziFillRiskJob", "info", "定时开关isJimuheziFillRisk关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isJimuheziFillRisk关闭，此次不执行");
		}


	}
	
    private void doFillRiskBatchJimuBatch(Date returnDate,Date doFillDate){
    	logger.info("积木风险金补足处理开始........");
    	loanLogService.createLog("JimuheziFillRiskJob", "info", "积木风险金补足处理开始........", "SYSTEM");
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("fundsSources", "积木盒子");
    	List<String> states = new ArrayList<String>();
    	states.add(LoanStateEnum.正常.toString());
    	states.add(LoanStateEnum.逾期.toString());
    	states.add(LoanStateEnum.结清.toString());
    	states.add(LoanStateEnum.预结清.toString());
    	param.put("states", states);
    	
    	List<Long> loanIds = jobFreeSqlDao.jimuFillRiskData(param);
    	
    	for (Long loanId : loanIds) {
    		try {
    			afterLoanService.fillJimuRisk(loanId, returnDate, doFillDate);
    		} catch (Exception e) {
    			logger.error(loanId +"&&"+e.getMessage());
				int length = e.getMessage().length();
    			loanLogService.createLog("JimuheziFillRiskJob", "error", loanId +"&&"+(length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage()), "SYSTEM");
    		}
    	}
    	logger.info("积木风险金补足处理结束");
    	loanLogService.createLog("JimuheziFillRiskJob", "info", "积木风险金补足处理结束", "SYSTEM");
    }
    
    private void passMessage(Date lastRepayDate, Date doFillDate){
//        List<String> list = DictionaryUtils.getList("jimu_fill_risk_message_receiver");
    	SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setCodeType("jimu_fill_risk_message_receiver");
		List<SysDictionary> list = sysDictionaryService.findDictionaryListByVo(sysDictionary);
		
        Calendar lastRepayDateCal = Calendar.getInstance();
        lastRepayDateCal.setTime(lastRepayDate);
        Calendar doFillDateCal = Calendar.getInstance();
        doFillDateCal.setTime(doFillDate);
        if(list.size()>0){
            String[] receivers = list.get(0).getCodeTitle().split(",");
            for(String s : receivers){
                BaseMessage message = new BaseMessage();
                message.setSendTime(new Date());
                message.setSender(employeeService.findByUserCode("admin").getId());
                message.setState("1");
                message.setType("0");
                message.setContent(lastRepayDateCal.get(Calendar.YEAR) + "年" + (lastRepayDateCal.get(Calendar.MONTH)+1) + "月，" + lastRepayDateCal.get(Calendar.DATE) + "号端积木风险金充值时间为："
                		+ doFillDateCal.get(Calendar.YEAR) + "年"+(doFillDateCal.get(Calendar.MONTH)+1) + "月" +doFillDateCal.get(Calendar.DATE) + "日");
                message.setTitle("积木风险金充值通知");
                message.setReceiver(employeeService.findByUserCode(s).getId());
                baseMessageService.inserBaseMessage(message);
                
            }
        }
    }
}
