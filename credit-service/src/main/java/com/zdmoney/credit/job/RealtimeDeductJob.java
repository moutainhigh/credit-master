package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.service.OfferTransactionServiceImpl;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 *  定时划扣触发器
 * @author 00232949
 *
 */
@Service
public class RealtimeDeductJob {
    
    private static Logger logger = Logger.getLogger(OfferTransactionServiceImpl.class);
    
    /**
     * 0是关闭，只要不是0就执行
     */
    private static final String isClosing = "0";
    
    @Autowired
    private IOfferInfoService offerInfoService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    @Autowired
    private IOfferConfigService offerConfigService;
    
    /**
     * 发送tpp划款
     */
    public void realtimeDeductExecute() {
        logger.info("发送报盘开始===============");
        loanLogService.createLog("RealtimeDeductJob", "info", "自动划扣开始........", "SYSTEM");
        String isAutoDeduct = sysParamDefineService.getSysParamValue("sysJob", "isAutoDeduct");
        if(!isClosing.equals(isAutoDeduct)){
            if(offerConfigService.isCanDebit()){
                offerInfoService.realtimeDeductByDate(Dates.getCurrDate());
            } else {
                loanLogService.createLog("RealtimeDeductJob", "info","realtimeDeductExecute不在执行时间范围内，此次不执行", "SYSTEM");
                logger.warn("realtimeDeductExecute不在执行时间范围内，此次不执行");
            }
        }else{
            loanLogService.createLog("RealtimeDeductJob", "info", "定时开关isAutoDeduct关闭，此次不执行", "SYSTEM");
            logger.warn("定时开关isAutoDeduct关闭，此次不执行");
        }
        loanLogService.createLog("RealtimeDeductJob", "info", "自动划扣结束........", "SYSTEM");
        logger.info("发送报盘结束===============");
    }
}
