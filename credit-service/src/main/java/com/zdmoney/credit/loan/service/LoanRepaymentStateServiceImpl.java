package com.zdmoney.credit.loan.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentStateDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentState;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentStateService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 历史还款状态
 * @author Charming
 *
 */
@Service
@Transactional
public class LoanRepaymentStateServiceImpl implements ILoanRepaymentStateService {
    
    private static final Logger logger = Logger.getLogger(LoanRepaymentStateServiceImpl.class);
    
    @Autowired
    private ILoanRepaymentStateDao loanRepaymentStateDao;
    
    @Autowired
    private IVLoanInfoService loanInfoService;
    
    @Autowired
    private ILoanRepaymentDetailDao loanRepaymentDetailDao;
    
    @Autowired
    private ISequencesService sequencesService;

    /**
     * 记录债权每月还款状态信息
     * @param currDate
     * @throws Exception 
     */
    @Override
    public void createLoanRepaymentState(final Date currDate) throws Exception {
        // 统计月份
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.MONTH, -1);
        final String reportMonth = Dates.getDateTime(c.getTime(), "yyyyMM");
        // 删除统计月份记录
        loanRepaymentStateDao.deleteByReportMonth(reportMonth);
 
        // 查询出需要记录的历史债权
        List<Long> loanIds = loanRepaymentStateDao.findHisLoans(currDate);
        if (CollectionUtils.isEmpty(loanIds)) {
            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆没有需要记录的历史债权");
            return;
        }
        logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆需要记录的历史债权条数：" + loanIds.size());     
        //分组记录状态
        int weight=10000;//多少个一组
        int group=loanIds.size()/weight+1; //分组
        List<List<Long>> loanIdsNew =new ArrayList<List<Long>>();
        for(int i=0;i<group;i++){  
           if(i<group-1){
        	   loanIdsNew.add(loanIds.subList(i*weight,(i+1)*weight));
           }else {  
        	   loanIdsNew.add(loanIds.subList(i*weight,loanIds.size()));
           }  
        }
        
        ExecutorService exec1 = Executors.newFixedThreadPool(loanIdsNew.size());
        List<Callable<Long>> callList = new ArrayList<Callable<Long>>();
        for (int z = 0;z < loanIdsNew.size();z++) {
        	final List<Long> innerLoanIds = loanIdsNew.get(z);
        	//可以定义每个线程返回的内容
            callList.add(new Callable<Long>() {
                    public Long call() throws Exception {
                    	logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆开启子线程 处理条数：" + innerLoanIds.size() + " 开始");
                    	loanInvkoer(innerLoanIds,reportMonth,currDate);
                    	logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆开启子线程 处理条数：" + innerLoanIds.size()+ "  结束");
                    	return 0L;
                     }
            });
        }               
        
        //开始执行线程
        List<Future<Long>> futureList = exec1.invokeAll(callList);
        //跟据上方返回的1做业务处理
        for (Future<Long> future : futureList) {
        	future.get();
        }
    }
    
    public void loanInvkoer(List<Long> loanIds,String reportMonth,Date currDate){
    	for (Long loanId : loanIds) {
            try {
                LoanRepaymentState loanRepaymentState = new LoanRepaymentState();
                VLoanInfo loanInfo = loanInfoService.findByLoanId(loanId);
                // 统计月份
                loanRepaymentState.setReportMonth(reportMonth);
                // 计算统计期数
                Long reportTerm = this.calculateReportTerm(currDate, loanInfo);
                loanRepaymentState.setReportTerm(reportTerm);
                // 还款期数
                Long repayTerm = loanRepaymentStateDao.findRepayTerm(loanId, currDate);
                if(loanInfo.getEndrdate().compareTo(Dates.format(Dates.getBeforeDays(Dates.getMonthFirstDay(currDate), 1), "yyyy-MM-dd"))<0){//已过最后一期，则取最后一期期数
                	repayTerm=loanInfo.getTime();
                }
                loanRepaymentState.setCurrentTerm(repayTerm);
                // 结清日期
                Date finishDate = loanRepaymentStateDao.findFinishDate(loanId);
                // 查询逾期起始日
                Date overdueDate = loanRepaymentStateDao.findOverdueDate(loanId, currDate);
                if (reportTerm <= 0) {// 如果统计期数小于等于零，则还款状态为：*
                    loanRepaymentState.setState("*");
                } else if (reportTerm > loanInfo.getTime()) {// 如果统计期数大于借款总期数
                    // 结清日期非空且和统计月份是同一个月
                    if (null != finishDate && Dates.getDateTime(finishDate, "yyyyMM").equals(reportMonth)) {
                    	if(Long.valueOf(Dates.getDateTime(finishDate, "dd"))<=(loanInfo.getPromiseReturnDate())){//结清日期小于等于还款日 C
                            loanRepaymentState.setState("C");
                    	}else{//大于还款日
                    		String overState =String.valueOf(Dates.diffMonths(currDate, overdueDate));
                            // 计算逾期状态
                            loanRepaymentState.setState(overState);
                    	}
                    } else {                       
                        if (null == overdueDate) {
                            loanRepaymentState.setState("C");
                        } else {
                        	String overState =String.valueOf(Dates.diffMonths(currDate, overdueDate));
                            // 计算逾期状态
                            loanRepaymentState.setState(overState);
                        }
                    }
                } else {// 如果统计期数介于还款期数之间 // 统计期数还款计划
                    LoanRepaymentDetail vo = new LoanRepaymentDetail();
                    vo.setLoanId(loanId);
                    vo.setCurrentTerm(reportTerm);
                    LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findListByVo(vo).get(0);
                    if (finishDate != null && Dates.compareTo(finishDate, loanRepaymentDetail.getReturnDate()) <= 0) {// 结清日期非空且小于等于应还款日期，还款状态是：C
                        // 当前和之后每一期全部填充C
                        leftRepaymentStateIsC(loanId, reportTerm, loanInfo.getTime(), reportMonth, repayTerm);
                        continue;
                    }else{
                    	if(null==overdueDate){
                    		loanRepaymentState.setState("N");
                    	}else{
                    		String overState =String.valueOf(Dates.diffMonths(currDate,overdueDate));
                    		loanRepaymentState.setState(overState);
                    	}                    	
                    }
                }
                loanRepaymentState.setLoanId(loanId);
                loanRepaymentState.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_STATE));
                loanRepaymentStateDao.insert(loanRepaymentState);
            } catch (Exception e) {
                logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆记录的历史债权错误！loanId：" + loanId+";统计月份："+reportMonth, e);
            }
        }
    }

    /**
     * 提前结清记录结清所处期数到还款最后期数的所有还款状态信息
     * @param loanId
     * @param reportTerm
     * @param time
     * @param reportMonth
     * @param currentTerm
     */
    private void leftRepaymentStateIsC(Long loanId, Long reportTerm, Long time, String reportMonth, Long currentTerm) {
        for (Long i = reportTerm; i <= time; i++) {
            LoanRepaymentState loanRepaymentState = new LoanRepaymentState();
            loanRepaymentState.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_STATE));
            loanRepaymentState.setLoanId(loanId);
            loanRepaymentState.setReportMonth(reportMonth);
            loanRepaymentState.setReportTerm(i);
            loanRepaymentState.setCurrentTerm(currentTerm);
            loanRepaymentState.setState("C");
            loanRepaymentStateDao.insert(loanRepaymentState);
        }
    }

    /**
     * 计算统计期数
     * @param currDate
     * @param loanInfo
     * @return
     */
    private long calculateReportTerm(Date currDate, VLoanInfo loanInfo) {
        // 还款起始日期
        Date startrdate = loanInfo.getStartrdate();
        // 统计日期取统计月份第一天
        Date date1 = Dates.format(currDate, "yyyy-MM");
        if (date1.compareTo(startrdate) <= 0) {
            return 0L;
        }
        Date date2 = Dates.format(startrdate, "yyyy-MM");
        return (long) Dates.diffMonths(date1, date2);
    }
}
