package com.zdmoney.credit.job;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.debit.service.pub.IDebitOfflineOfferInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 外贸3需求相关定时任务处理类
 * 后续外贸3新增的定时任务都加入到这个类中
 * @author 00236640
 */
@Service
public class WM3BusinessJob {

    private static final Logger logger = Logger.getLogger(WM3BusinessJob.class);

    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IDebitOfflineOfferInfoService debitOfflineOfferInfoService;
    /**
     * 调用外贸信托（线下实收【 2312】）接口、发起线下还款溢交款账户充值，
     * 触发频率：每天19点执行一次
    */
    public void executeOfflineDebit(){
        logger.info("调用外贸3线下还款溢交款接口定时任务执行开始。。。。。。");
        String isExecute = sysParamDefineService.getSysParamValue("sysJob", "executeOfflineDebit");
        if (!Const.isClosing.equals(isExecute)) {
            debitOfflineOfferInfoService.pushOfflineDebit4WM3();
        }
        logger.info("调用外贸3线下还款溢交款接口定时任务执行结束。。。。。。");
    }
}
