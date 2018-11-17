package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.overdue.dao.pub.IOverdueRatioStatDao;
import com.zdmoney.credit.overdue.domain.OverdueRatioStat;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
@Service
public class OverdueRatioStatJob {
	
	private static final Logger logger = Logger.getLogger(OverdueRatioStatJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IJobFreeSqlDao jobFreeSqlDao;
	@Autowired
	private IOverdueRatioStatDao overdueRatioStatDao;
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private ILoanLogService loanLogService;

	public void execute() {
    	String isOverdueRatioStat = sysParamDefineService.getSysParamValue("sysJob", "isOverdueRatioStat");
		if(!Const.isClosing.equals(isOverdueRatioStat)){
			logger.info("OverdueRatioStatJob开始........");
			loanLogService.createLog("OverdueRatioStatJob", "info", "OverdueRatioStatJob开始........", "SYSTEM");
			
			String todayStr = Dates.getDateTime(new Date(), Dates.DEFAULT_DATE_FORMAT);
			if ((new Date()).before(Dates.parse("2013-03-18", Dates.DEFAULT_DATE_FORMAT))) {
				jobFreeSqlDao.overdueRatioStatJobDelete(todayStr);
				List<Map<String, Object>> depResult = jobFreeSqlDao.orsjSelectData();
				
				Map<String, String> odrssMap = new HashMap<String, String>();
				
				for (Map<String, Object> depMap : depResult) {
					String depName = (String)depMap.get("DEPNAME");
					String loanType = (String)depMap.get("LOANTYPE");
					if (odrssMap.containsKey(depName + loanType)) {
	                    List<Long> thirty = new ArrayList<Long>();
	                    List<Long> sixty = new ArrayList<Long>();
	                    List<Long> ninty = new ArrayList<Long>();
	                    List<Long> aboveNinty = new ArrayList<Long>();
	                    List<Long> above180 = new ArrayList<Long>();
	                    BigDecimal overdue30 = BigDecimal.ZERO;
	                    BigDecimal overdue60 = BigDecimal.ZERO;
	                    BigDecimal overdue90 = BigDecimal.ZERO;
	                    BigDecimal overdueAbove90 = BigDecimal.ZERO;
	                    BigDecimal overdueAbove180 = BigDecimal.ZERO;
						
						String code = (String)depMap.get("DEPID");
						Map<String, Object> param1 = new HashMap<String, Object>();
						param1.put("depId", code);
						param1.put("loanType", loanType);
						
						List<Long> loanIds = jobFreeSqlDao.orsjSelectLoanId(param1);
						

	                    List<String> repaymentStatesStrs = new ArrayList<String>();
	                    repaymentStatesStrs.add(RepaymentStateEnum.未还款.toString());
	                    repaymentStatesStrs.add(RepaymentStateEnum.不足额还款.toString());
	                    repaymentStatesStrs.add(RepaymentStateEnum.不足罚息.toString());

	                    for (Long loanId : loanIds) {
	                    	
	                    	Map<String, Object> param2 = new HashMap<String, Object>();
	                    	param2.put("loanId", loanId);
	                    	param2.put("repaymentStates", repaymentStatesStrs);
	                        List<Date> returnDates = jobFreeSqlDao.orsjSelectReturnDate(param2);
	                        double maxDay = 0;
	                        if (returnDates.size() > 0) {
	                        	try {
	    							maxDay = calculate(Dates.getDateTime(returnDates.get(0), Dates.DEFAULT_DATE_FORMAT),
	    									todayStr);
	    						} catch (Exception e) {
	    							e.printStackTrace();
	    						}
	                        }
	                        
	                        if (1 <= maxDay && maxDay <= 30) {
	                        	thirty.add(loanId);
	                        } else if (31 <= maxDay && maxDay <= 60) {
	                        	sixty.add(loanId);
	                        } else if (61 <= maxDay && maxDay <= 90) {
	                        	ninty.add(loanId);
	                        } else if (91 <= maxDay && maxDay <= 180) {
	                        	aboveNinty.add(loanId);
	                        } else if (181 <= maxDay) {
	                        	above180.add(loanId);
	                        }
	                    }
	                    
	                    if (thirty.size() > 0) {
	                    	Map<String, Object> param3 = new HashMap<String, Object>();
	    					param3.put("depId", code);
	    					param3.put("loanType", loanType);
	    					param3.put("loanIds", thirty);
	    					String sum = jobFreeSqlDao.orsjSelectOverdueSum(param3);
	    					overdue30 = new BigDecimal(sum);
	                    }
	                    
	                    if (sixty.size() > 0) {
	                    	Map<String, Object> param3 = new HashMap<String, Object>();
	    					param3.put("depId", code);
	    					param3.put("loanType", loanType);
	    					param3.put("loanIds", sixty);
	    					String sum = jobFreeSqlDao.orsjSelectOverdueSum(param3);
	    					overdue60 = new BigDecimal(sum);
	                    }
	                    
	                    if (ninty.size() > 0) {
	                    	Map<String, Object> param3 = new HashMap<String, Object>();
	    					param3.put("depId", code);
	    					param3.put("loanType", loanType);
	    					param3.put("loanIds", ninty);
	    					String sum = jobFreeSqlDao.orsjSelectOverdueSum(param3);
	    					overdue90 = new BigDecimal(sum);
	                    }
	                    
	                    if (aboveNinty.size() > 0) {
	                    	Map<String, Object> param3 = new HashMap<String, Object>();
	    					param3.put("depId", code);
	    					param3.put("loanType", loanType);
	    					param3.put("loanIds", aboveNinty);
	    					String sum = jobFreeSqlDao.orsjSelectOverdueSum(param3);
	    					overdueAbove90 = new BigDecimal(sum);
	                    }
	                    
	                    if (above180.size() > 0) {
	                    	Map<String, Object> param3 = new HashMap<String, Object>();
	    					param3.put("depId", code);
	    					param3.put("loanType", loanType);
	    					param3.put("loanIds", above180);
	    					String sum = jobFreeSqlDao.orsjSelectOverdueSum(param3);
	    					overdueAbove180 = new BigDecimal(sum);
	                    }
	                    BigDecimal restAmount = new BigDecimal((String)depMap.get("REST_AMOUNT"));
//	                    BigDecimal restOverdueAmount = new BigDecimal((String)depMap.get("rest_overdue_amount"));
	                    Date today = new Date();
	                    String depType = (String)depMap.get("TYPE");
	                    
	                    OverdueRatioStat os30 = new OverdueRatioStat();
	                    os30.setYinyebu(depName);
	                    os30.setShengyubenjin(restAmount);
	                    os30.setYuqijine(overdue30);
	                    os30.setYuqilv(overdue30.divide(restAmount, MathContext.DECIMAL128));
	                    os30.setCurDate(today);
	                    os30.setYinyuebucode(code);
	                    os30.setDaikuanleixing(loanType);
	                    os30.setYuqitianshu("1天-30天");
	                    os30.setYinyuebuleixing(depType);
	                    os30.setId(sequencesService.getSequences(SequencesEnum.OVERDUE_RATIO_STAT));
	                    overdueRatioStatDao.insert(os30);
	                                       
	                    OverdueRatioStat os60 = new OverdueRatioStat();
	                    os60.setYinyebu(depName);
	                    os60.setShengyubenjin(restAmount);
	                    os60.setYuqijine(overdue60);
	                    os60.setYuqilv(overdue60.divide(restAmount, MathContext.DECIMAL128));
	                    os60.setCurDate(today);
	                    os60.setYinyuebucode(code);
	                    os60.setDaikuanleixing(loanType);
	                    os60.setYuqitianshu("31天-60天");
	                    os60.setYinyuebuleixing(depType);
	                    os60.setId(sequencesService.getSequences(SequencesEnum.OVERDUE_RATIO_STAT));
	                    overdueRatioStatDao.insert(os60);
	                    
	                    OverdueRatioStat os90 = new OverdueRatioStat();
	                    os90.setYinyebu(depName);
	                    os90.setShengyubenjin(restAmount);
	                    os90.setYuqijine(overdue90);
	                    os90.setYuqilv(overdue90.divide(restAmount, MathContext.DECIMAL128));
	                    os90.setCurDate(today);
	                    os90.setYinyuebucode(code);
	                    os90.setDaikuanleixing(loanType);
	                    os90.setYuqitianshu("61天-90天");
	                    os90.setYinyuebuleixing(depType);
	                    os90.setId(sequencesService.getSequences(SequencesEnum.OVERDUE_RATIO_STAT));
	                    overdueRatioStatDao.insert(os90);
	                    
	                    OverdueRatioStat osAbove90 = new OverdueRatioStat();
	                    osAbove90.setYinyebu(depName);
	                    osAbove90.setShengyubenjin(restAmount);
	                    osAbove90.setYuqijine(overdueAbove90);
	                    osAbove90.setYuqilv(overdueAbove90.divide(restAmount, MathContext.DECIMAL128));
	                    osAbove90.setCurDate(today);
	                    osAbove90.setYinyuebucode(code);
	                    osAbove90.setDaikuanleixing(loanType);
	                    osAbove90.setYuqitianshu("91天-180天");
	                    osAbove90.setYinyuebuleixing(depType);
	                    osAbove90.setId(sequencesService.getSequences(SequencesEnum.OVERDUE_RATIO_STAT));
	                    overdueRatioStatDao.insert(osAbove90);
	                    
	                    OverdueRatioStat osAbove180 = new OverdueRatioStat();
	                    osAbove180.setYinyebu(depName);
	                    osAbove180.setShengyubenjin(restAmount);
	                    osAbove180.setYuqijine(overdueAbove180);
	                    osAbove180.setYuqilv(overdueAbove180.divide(restAmount, MathContext.DECIMAL128));
	                    osAbove180.setCurDate(today);
	                    osAbove180.setYinyuebucode(code);
	                    osAbove180.setDaikuanleixing(loanType);
	                    osAbove180.setYuqitianshu("180天以上");
	                    osAbove180.setYinyuebuleixing(depType);
	                    osAbove180.setId(sequencesService.getSequences(SequencesEnum.OVERDUE_RATIO_STAT));
	                    overdueRatioStatDao.insert(osAbove180);
	                    

	                    odrssMap.put(depName + loanType, depName + loanType);
					}
				}
			} else {
				List<Integer> checkResult = jobFreeSqlDao.orsjSelectCheck();
				if (checkResult.size() > 0) {
					return;
				}
				
				try {
					jobFreeSqlDao.orsjInsertRatioNew();
				} catch (Exception e) {
					logger.error(e.getMessage());
					int length = e.getMessage().length();
					loanLogService.createLog("OverdueRatioStatJob", "error", length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage(), "SYSTEM");
				}
				
				
			}
			
			logger.info("OverdueRatioStatJob结束........");
			loanLogService.createLog("OverdueRatioStatJob", "info", "OverdueRatioStatJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("OverdueRatioStatJob", "info", "定时开关isOverdueRatioStat关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isOverdueRatioStat关闭，此次不执行");
		}
		

	}
	
    private double calculate(String date1, String date2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(date1);
        Date end = sdf.parse(date2);
        long delta = end.getTime() - start.getTime();
        double result = delta * 1.0 / (1000 * 60 * 60*24);
        return result;
    }
//    
//    public static void main(String[] args) {
//    	System.out.println(RepaymentStateEnum.未还款.toString());
//    	
//    }

}
