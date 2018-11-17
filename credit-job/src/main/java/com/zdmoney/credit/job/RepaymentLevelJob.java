package com.zdmoney.credit.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ymkj.rule.biz.api.message.MapResponse;
import com.ymkj.rule.biz.api.message.Response;
import com.ymkj.rule.biz.api.message.RuleEngineRequest;
import com.ymkj.rule.biz.api.service.IRuleEngineExecuter;
import com.ymkj.rule.biz.api.vo.CoreAccountCategoryExecVo;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentLevelHis;
import com.zdmoney.credit.loan.domain.RepaymentLevelLoan;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentLevelHisService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * @ClassName:     RepaymentLevelJob.java
 * @Description:   还款等级查询，调用规则引擎服务
 * @author         guocl
 * @version        V2.0  
 * @Date           2017年5月31日
 */
@Service
public class RepaymentLevelJob {
	private static final Logger logger = Logger.getLogger(RepaymentLevelJob.class);
	@Autowired
	IRuleEngineExecuter ruleEngineExecuter;

    @Autowired
    @Qualifier("VLoanInfoServiceImpl")
    IVLoanInfoService vLoanInfoServiceImpl;
    
    @Autowired
    ILoanRepaymentLevelHisService loanRepaymentLevelHisService;

    @Autowired
    @Qualifier("loanRepaymentDetailServiceImpl")
    ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
    @Autowired
    private ILoanLogService loanLogService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void execute() {
		logger.info("开始执行生成客户还款等级批处理任务(repaymentLevelJob)。。。");
		loanLogService.createLog("repaymentLevelJob", "info","执行生成客户还款等级批处理任务开始........", "SYSTEM");
	    String loanStateParams = sysParamDefineService.getSysParamValue("sysJob", "loanStateParamsForJob");
	    String[] loanStateParam = loanStateParams.split(",");
		
	    StopWatch sw = new StopWatch();
        sw.start();
	    ExecutorService executor = Executors.newFixedThreadPool(10);
		 
		for(int page=1;;page++){
		    StopWatch seg = new StopWatch();
		    seg.start();
		    
			Map<String, Object> params=new HashMap<String, Object>();
			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(50);
			pager.setSidx("ID");
			pager.setSort("ASC");
			params.put("pager", pager);
			params.put("loanStateParams", loanStateParam);
			
			pager =  vLoanInfoServiceImpl.getLoanVoByStateWithPg(params); 		
			final List<RepaymentLevelLoan> repaymentLevelLoans = pager.getResultList();
			
			if(repaymentLevelLoans == null || repaymentLevelLoans.isEmpty()){
				break;
			}
			
			executor.execute(new Runnable(){
                public void run(){
                    for(RepaymentLevelLoan repaymentLevelLoan:repaymentLevelLoans){
                        try {
                            List<LoanRepaymentDetail> repaymentDetailList = new ArrayList<LoanRepaymentDetail>();
                            if (Strings.isNotEmpty(repaymentLevelLoan.getId())) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("loanId", repaymentLevelLoan.getId());
                                repaymentDetailList = loanRepaymentDetailServiceImpl.findByLoanIdAndRepaymentState(map);
                            }
                            //如果还款计划表里没有查到任何计划，则跳出循环，不再查询该借款ID的还款等级
                            if(repaymentDetailList == null || repaymentDetailList.isEmpty()){
                                continue;
                            }
                            RuleEngineRequest<CoreAccountCategoryExecVo> request = new RuleEngineRequest<CoreAccountCategoryExecVo>();
                            request.setBizType("coreAccountCategory");
                            request.setSysCode("0001");
                            
                            CoreAccountCategoryExecVo vo = new CoreAccountCategoryExecVo();
                            vo.setIdNo(repaymentLevelLoan.getIdNum());
                            vo.setPeriod(repaymentDetailList.size());
                            
                            List<Integer> repaymentStatues = new ArrayList<Integer>();
                            List<Date> shouldRepaymentDates = new ArrayList<Date>();
                            List<Date> realityRepaymentDates = new ArrayList<Date>();
                            for(LoanRepaymentDetail data:repaymentDetailList){
                                switch(data.getRepaymentState()){
                                    case "结清": 
                                        repaymentStatues.add(0);//结清
                                        break;  
                                    case "不足罚息":
                                        repaymentStatues.add(1);//逾期
                                        break;
                                    case "不足额还款":
                                        logger.info("不足额还款，需要根据时间判断，是否逾期or未还款");
                                    case "未还款":
                                        if(data.getReturnDate() != null && data.getReturnDate().before(new Date())){
                                            repaymentStatues.add(1);//逾期
                                        }else{
                                            repaymentStatues.add(2);//未还款
                                        }
                                        break;
                                    default:  
                                        break; 
                                }
                                
                                if(data.getReturnDate() != null){
                                    shouldRepaymentDates.add(data.getReturnDate());
                                }
                                if(data.getFactReturnDate() !=null){
                                    realityRepaymentDates.add(data.getFactReturnDate());
                                }else{
                                    realityRepaymentDates.add(DateUtils.parseDate("9999-12-31", new String[] {"yyyy-MM-dd"}));
                                }
                            }
                            vo.setRepaymentStatue(repaymentStatues);
                            vo.setShouldRepaymentDate(shouldRepaymentDates);
                            vo.setRealityRepaymentDate(realityRepaymentDates);
                            
                            request.setData(vo);
                            
                            logger.debug(Thread.currentThread().getName() + "-->借款号:"+repaymentLevelLoan.getId()+ "--> rules request message:"+JSON.toJSONString(request));
                            Response response =  ruleEngineExecuter.executeRuleEngine(request);
                            logger.debug(Thread.currentThread().getName() + "-->借款号:"+repaymentLevelLoan.getId()+ "--> rules response message:"+JSON.toJSONString(response));
                            
                            if(response != null && "000000".equals(response.getRepCode())){
                                MapResponse mp = MapResponse.class.cast(response);
                                
                                Map<String,Object> map = mp.getMap();
                                
                                String accountClassificationForWebpage = (String) map.get("AccountClassificationForWebpage");
                                
                                StringBuffer acrs = new StringBuffer();
                                StringBuffer cls = new StringBuffer();
                                
                                for(int i = 1; i<=repaymentDetailList.size(); i++){
                                    StringBuffer accountClassificationForReportingSystems = new StringBuffer((String) map.get("AccountClassificationForReportingSystems[" +i + "]"));
                                    
                                    /*StringBuffer customerLevel = new StringBuffer((String) map.get("CustomerLevel[" +i + "]"));*/
                                    
                                    acrs.append("#").append(accountClassificationForReportingSystems);
                                    
                                    /*cls.append("#").append(customerLevel);*/
                                }
                                
                                logger.debug("借款号:"+repaymentLevelLoan.getId()+"在["+new SimpleDateFormat("yyyy-MM").format(new Date())+"]月还款等级为:"+accountClassificationForWebpage);
                                
                                LoanRepaymentLevelHis his = new LoanRepaymentLevelHis();
                                his.setLoanId(repaymentLevelLoan.getId());
                                his.setCurrentTerm(new SimpleDateFormat("yyyy-MM").format(new Date()));
                                his.setRepayLevel(accountClassificationForWebpage);
                                
                                his.setAccountClassificationForWebpage(accountClassificationForWebpage);
                                his.setAccountClassificationForReportingSystems(acrs.toString());
                                his.setCustomerLevel(cls.toString());
                                
                                his.setCreateTime(new Date());
                                loanRepaymentLevelHisService.create(his);
                            }else{
                                logger.error(Thread.currentThread().getName() + "-->借款号:"+repaymentLevelLoan.getId()+ "--> rules request  message:"+JSON.toJSONString(request));
                                logger.error(Thread.currentThread().getName() + "-->借款号:"+repaymentLevelLoan.getId()+ "--> rules response message:"+JSON.toJSONString(response));
                            }
                        } catch (Exception ex) {
                            /** 系统忙 **/
                            logger.error(ex.getMessage(),ex);
                        }
                    }
                }
            });
			
			seg.stop();
	        logger.debug(((page-1)*50 + 1)+"至"+page*50+"条记录共耗时："+sw.getTime()/1000 + "秒");
	        
			if(repaymentLevelLoans.size()<50){
				break;
			}
		}
		sw.stop();
		logger.info("生成客户还款等级批处理任务(repaymentLevelJob)结束-->共耗时："+sw.getTime()/1000*60 + "分钟");
		
		loanLogService.createLog("repaymentLevelJob", "info","生成客户还款等级批处理任务结束........", "SYSTEM");
	}
		

}
