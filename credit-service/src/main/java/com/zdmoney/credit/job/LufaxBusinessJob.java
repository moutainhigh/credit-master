package com.zdmoney.credit.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailLufaxService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 陆金所需求相关定时任务处理类
 * @author 00236640
 *
 */
@Service
public class LufaxBusinessJob {

    private static final Logger logger = Logger.getLogger(LufaxBusinessJob.class);

    @Autowired
    private ILoanRepaymentDetailService loanRepaymentDetailService;

    @Autowired
    private IDebitQueueLogService debitQueueLogService;


    @Autowired
    private ISplitQueueLogService splitQueueLogService;

    @Autowired
    private ISysParamDefineService sysParamDefineService;

    @Autowired
    private ILoanRepaymentDetailLufaxService loanRepaymentDetailLufaxService;
    /**
     * 调用【转发代扣】接口推送代扣信息给陆金所，触发频率：每天16点至20点间隔30分钟执行一次
     * 推送数据范围：
     * 1、前一天委托还款，进入划扣队列的数据
     * 2、前一天提前结清还款，进入划扣队列的数据
     * 3、当天逾期代偿，进入划扣队列的数据
     */
    public void entrustDebit(){
        logger.info("调用陆金所提供的【转发代扣】接口定时任务执行开始。。。。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "executeEntrustDebit");
        if (!Const.isClosing.equals(isExecute)) {
        	// 提前结清和委托还款转发代扣
            /*try {
                debitQueueLogService.executeAdvanceClearDebit(null);
            } catch (Exception e) {
                logger.error("※※※※※提前结清转发代扣※※※※※"+e.getMessage(), e);
            }*/
            // 逾期代偿和委托还款转发代扣
            try {
                debitQueueLogService.overdueCompensatory(null,"reserve");
            } catch (Exception e) {
                logger.error("※※※※※逾期代偿转发代扣※※※※※"+e.getMessage(), e);
            }
            // 一次性回购转发代扣
            try{
                debitQueueLogService.oneTimeOverdueBuyBack(null);
            } catch (Exception e) {
                logger.error("※※※※※一次性回购转发代扣※※※※※"+e.getMessage(), e);
            }
        }
        logger.info("调用陆金所提供的【转发代扣】接口定时任务执行结束。。。。。。");
    }

    /**
     * 陆金所（还款计划、还款明细、还款记录）数据推送，触发频率：每天间隔30分钟执行一次
     * 推送数据范围：
     * 1、机构还款金额冻结成功，进入分账队列的数据
     * 2、委托还款金额冻结成功，进入分账队列的数据
     * 3、提前结清还款金额冻结成功，进入分账队列的数据
     * 4、逾期代偿金额冻结成功，进入分账队列的数据
     */
    public void syncEntrustRepaymentInfo(){
        logger.info("同步陆金所（还款计划、还款明细、还款记录）定时任务执行开始。。。。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "executeEntrustRepaymentInfo");
        if (!Const.isClosing.equals(isExecute)) {
            // 机构还款（还款计划、还款明细、还款记录）数据推送
            try {
                splitQueueLogService.executeOrganRepaymentInfo(null);
            } catch (Exception e) {
                logger.error("※※※※※机构还款（还款计划、还款明细、还款记录）数据推送※※※※※"+e.getMessage(), e);
            }
            // 提前结清还款（还款计划、还款明细、还款记录）数据推送
            try {
                splitQueueLogService.executeAdvanceClearRepaymentInfo(null);
            } catch (Exception e) {
                logger.error("※※※※※提前结清还款（还款计划、还款明细、还款记录）数据推送※※※※※"+e.getMessage(), e);
            }
            // 逾期代偿（还款计划、还款明细、还款记录）数据推送
            try {
                splitQueueLogService.syncOverdueCompensatoryInfo(null);
            } catch (Exception e) {
                logger.error("※※※※※逾期代偿（还款计划、还款明细、还款记录）数据推送※※※※※"+e.getMessage(), e);
            }
            // 一次性回购（还款计划、还款明细、还款记录）数据推送
            try {
                splitQueueLogService.executeOneBuyBackRepaymentInfo(null);
            } catch (Exception e) {
            	logger.error("※※※※※一次性回购（还款计划、还款明细、还款记录）数据推送※※※※※"+e.getMessage(), e);
            }
        }
        logger.info("同步陆金所（还款计划、还款明细、还款记录）定时任务执行结束。。。。。。");
    }

    /**
     * 还款日后一天挂账金额分账，触发频率：每月2号、17号 1点整执行一次
     */
    public void billRepayDeal(){
        logger.info("陆金所还款日后一天挂账金额分账定时任务执行开始。。。。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "executeBillRepayDeal");
        if (!Const.isClosing.equals(isExecute)) {
            debitQueueLogService.executeBillRepayDeal(null);
        }
        logger.info("陆金所还款日后一天挂账金额分账定时任务执行结束。。。。。。");
    }

    /**
     * 保存逾期代偿划扣队列数据，触发频率：每月2号、17号 0:20分执行一次
     */
    public void perpareOverdueCompensatory(){
        logger.info("保存逾期代偿/一次性回购 队列开始。。。。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "perpareOverdueCompensatory");
        if (!Const.isClosing.equals(isExecute)) {
            debitQueueLogService.perpareOverdueCompensatory();
        }
        logger.info("保存逾期代偿/一次性回购 队列结束。。。。。。");
    }

    /**
     * 逾期还回、陆金所（还款计划、还款记录）数据推送，触发频率：每天 0:30分执行一次
     */
    public void syncOverdueRepaymentInfo(){
        logger.info("同步【逾期还回】（还款计划、还款记录）开始。。。。。。");
        String isPushOverdueRepay2LufaxJob = sysParamDefineService.getSysParamValue("sysJob","isPushOverdueRepay2LufaxJob");
        if(Const.isClosing.equals(isPushOverdueRepay2LufaxJob)){
            logger.info("isPushOverdueRepay2LufaxJob，此次不执行。。");
            return;
        }
        try{
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("sendEntrustFlags", new String[] {"0","2"});//代扣成功  分账失败
            params.put("splitResultStates", new String[]{SplitResultStateEnum.未分账.getCode(),SplitResultStateEnum.分账失败.getCode()});
            List<SplitQueueLog> list = splitQueueLogService.findOverdueEntrustSplitQueueLogList(params);
            if(CollectionUtils.isEmpty(list)){
                logger.info("PushOverdueRepay2LufaxJob，没有待推送的逾期还回信息");
                return;
            }
            HashSet<String> splitIds = new HashSet<String>();
            for(SplitQueueLog splitQueueLog: list){
                splitIds.add(splitQueueLog.getId().toString());
                splitQueueLogService.updateRepaymentDetailAndLoanStatus4Lufax(splitQueueLog);
            }
            splitQueueLogService.executeOverdueRepaymentInfo(splitIds);
        }catch(Exception e){
            logger.error("PushOverdueRepay2LufaxJob执行异常",e);
        }
        logger.info("同步【逾期还回】（还款计划、还款记录）结束。。。。。。");
    }

    /**
     * 推送逾期还款计划 每天1:10 执行一次
     */
    public void pushOverdueRepaymentPlan(){
        logger.info("推送逾期还款计划至陆金所开始。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "isPushOverdueRepaymentPlan");
        if (!Const.isClosing.equals(isExecute)) {
            loanRepaymentDetailLufaxService.pushOverdueRepaymentPlan();
        }
        logger.info("推送逾期还款计划至陆金所结束。。。。。。");
    }

    /**
     * 证大同步追偿还款记录和还款计划给陆金所
     * 证大同步一次性代偿、最后一期代偿追偿的还款记录和还款计划
     */
    public void executeOneBuyBackAndCompensatedRepayInfo(){
        logger.info("证大同步追偿还款记录和还款计划给陆金所开始。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "isExecuteOneBuyBackAndCompensatedLufax");
        if (!Const.isClosing.equals(isExecute)) {
        	splitQueueLogService.syncOneBuyBackAndCompensatedRepayInfo();
        }
        logger.info("证大同步追偿还款记录和还款计划给陆金所结束。。。");
    }
    
}
