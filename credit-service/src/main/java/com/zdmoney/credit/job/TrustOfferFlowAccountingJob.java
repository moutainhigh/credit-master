package com.zdmoney.credit.job;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.trustOffer.service.TrustOfferFlowServiceImpl;
import com.zdmoney.credit.trustOffer.service.pub.ITrustOfferFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ym10094 on 2016/10/19.
 */
@Service
public class TrustOfferFlowAccountingJob {
    private Logger logger = LoggerFactory.getLogger(TrustOfferFlowAccountingJob.class);
    @Autowired
    private ITrustOfferFlowService trustOfferFlowServiceImpl;
    @Autowired
    private ISysParamDefineService sysParamDefineService;

    public void histroyRepayInfosExcute(String beginDate,String endDate){
        logger.info("开始执行TrustOfferFlowAccountingJob中的{}","histroyRepayInfosExcute");
        String isHistoryTrustSliptAccounting = sysParamDefineService.getSysParamValue("sysJob", "isHistoryTrustSliptAccounting");
        if(Strings.isEmpty(isHistoryTrustSliptAccounting) || "0".equals(isHistoryTrustSliptAccounting)){
            logger.info("histroyRepayInfosExcute执行关闭！");
            return ;
        }
        List<OfferRepayInfo> repayInfos = trustOfferFlowServiceImpl.getTrustRepayMentOfferRepayInfoHistroy(beginDate,endDate);
        this.repayJobDealService(repayInfos);
    }
    public void yesterDayRepayInfosExcute(){
        logger.info("开始执行TrustOfferFlowAccountingJob中的{}","yesterDayRepayInfosExcute");
        List<OfferRepayInfo> repayInfos = trustOfferFlowServiceImpl.getTrustRepayMentOfferRepayInfoYesterDay();
        this.repayJobDealService(repayInfos);
    }

    public void repayJobDealService(List<OfferRepayInfo> repayInfos){
        String isTrustSliptAccounting = sysParamDefineService.getSysParamValue("sysJob", "isTrustSliptAccounting");
        if(Strings.isEmpty(isTrustSliptAccounting) || "0".equals(isTrustSliptAccounting)){
            logger.info("TrustOfferFlowAccountingJob执行关闭！");
            return ;
        }
        logger.info("开始信托还款记账处理：");
        logger.info("本次处理的还款记录条数："+repayInfos.size());
        if (CollectionUtils.isEmpty(repayInfos)) {
            logger.info("本次信托分账记账处理没有要处理的还款记录！");
            return;
        }
        for(OfferRepayInfo repayInfo:repayInfos){
            trustOfferFlowServiceImpl.trustRepayService(repayInfo);
        }
        TrustOfferFlowServiceImpl.relieflAmountMap.clear();
        logger.debug("清理缓存！");
        logger.info("信托还款记账处理结束！");
    }
}
