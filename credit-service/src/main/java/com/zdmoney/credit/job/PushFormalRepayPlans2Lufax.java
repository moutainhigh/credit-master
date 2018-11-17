package com.zdmoney.credit.job;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推送正式还款计划 借据 信息至陆金所
 * Created by YM10098 on 2017/7/11.
 */
@Service
public class PushFormalRepayPlans2Lufax {
    private Logger logger = Logger.getLogger(PushFormalRepayPlans2Lufax.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IOperateLogService operateLogService;
    @Autowired
    private ISplitQueueLogService splitQueueLogService;
    public void execute(){
        String isPushFormalRepayPlans2Lufax = sysParamDefineService.getSysParamValue("sysJob", "isPushFormalRepayPlans2Lufax");
        logger.info("推送正式还款计划借据信息至陆金所,job开始...");
        if(Const.isClosing.equals(isPushFormalRepayPlans2Lufax)){
            logger.info("推送正式还款计划借据信息至陆金所,此次job不执行...");
            return;
        }
        List<Long> loanIds = operateLogService.findLoanIds4FormalRepayPlans2Lufax();
        if(CollectionUtils.isEmpty(loanIds)){
            logger.info("推送正式还款计划借据信息至陆金所,没有待同步的数据...");
            return;
        }
        for(Long loanId : loanIds){
            try {
                splitQueueLogService.pushRepayPlanAndLoanInfo2Lufax(loanId);
                operateLogService.addOperateLog(loanId, "07", "1");
            }catch (Exception e){
                logger.info("推送正式还款计划借据信息至陆金所异常..."+e);
            }
        }
        logger.info("推送正式还款计划借据信息至陆金所,此次job执行完成...");
    }
}
