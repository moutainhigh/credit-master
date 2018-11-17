package com.zdmoney.credit.job;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 减免申请状态变更
 * Created by YM10098 on 2017/6/27.
 */
@Service
public class ApplyReliefStatusUpdateJob {

    private static Logger logger = Logger.getLogger(ApplyReliefStatusUpdateJob.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ISpecialRepaymentApplyService specialRepaymentApplyService;
    public void execute(){
        logger.info("减免申请状态变更job开始执行...");
        if(Const.isClosing.equals(sysParamDefineService.getSysParamValue("sysJob", "isApplyReliefStatusUpdate"))){
            logger.warn("定时开关isApplyReliefStatusUpdate关闭，减免申请状态变更job此次不执行...");
            return;
        }
        specialRepaymentApplyService.executeApplyReliefStatusUpdate();
        logger.info("减免申请状态变更job执行结束...");
    }
}
