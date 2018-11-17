package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.debit.service.pub.IRechargeSearchService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 外贸3 扣款结果查询job
 * @author YM10112
 *
 */
@Service
public class YiJiaoKuanRechargeSearchWm3Job {
    
    private static final Logger logger = Logger.getLogger(YiJiaoKuanRechargeSearchWm3Job.class);

    @Autowired
    private IRechargeSearchService rechargeSearchServiceImpl;

    @Autowired
    private ISysParamDefineService sysParamDefineService;

    public void rechargeResultSerachExecute() {
        // 外贸3扣款查询job开关（1：开关开启、其余情况：开关关闭）
        String isExcute = sysParamDefineService.getSysParamValue("sysJob","searchWm3DebitResult");
        if (!Const.isOpening.equals(isExcute)) {
            logger.info("【外贸3】扣款结果查询job已关闭...");
            return;
        }
        logger.info("【外贸3】扣款结果查询job开始执行...");
        try {
            rechargeSearchServiceImpl.getSearchResult();
        } catch (Exception e) {
            logger.error("【外贸3】扣款结果查询失败！" + e.getMessage(), e);
        }
        logger.info("【外贸3】扣款结果查询job执行结束...");
    }
}
