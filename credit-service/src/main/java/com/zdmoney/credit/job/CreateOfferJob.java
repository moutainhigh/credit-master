package com.zdmoney.credit.job;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 创建报盘文件
 * @author 00232949
 *
 */
@Service
@Transactional
public class CreateOfferJob {

    private static final Logger logger = Logger.getLogger(CreateOfferJob.class);

    @Autowired
    private IOfferInfoService offerInfoService;

    @Autowired
    private IComOrganizationService comOrganizationService;

    @Autowired
    private ILoanLogService loanLogService;

    @Autowired
    private ISysParamDefineService sysParamDefineService;

    @Autowired
    private IOfferCreateService offerCreateService;

    @Autowired
    private IOfferConfigService offerConfigService;

    /**
     * 创建逾期用户的报盘文件
     */
    public void createOverdueOffer() {
        try {
            logger.info("创建逾期报盘开始===============");
            loanLogService.createLog("CreateOfferJob", "info","创建逾期报盘开始........", "SYSTEM");
            String isAutoDeduct = sysParamDefineService.getSysParamValue("sysJob", "isCreateOverdueOffer");
            if (!Const.isClosing.equals(isAutoDeduct)) {
                if (offerConfigService.isCanCreateOverdueOffer()) {
                    int threadCount = 10;
                    multithreadingCreateOfferByType(threadCount,IOfferInfoService.YUQI);
                } else {
                    loanLogService.createLog("CreateOfferJob", "info","createOverdueOffer不在执行时间范围内，此次不执行", "SYSTEM");
                    logger.warn("createOverdueOffer不在执行时间范围内，此次不执行");
                }
            } else {
                loanLogService.createLog("CreateOfferJob", "info","定时开关isCreateOverdueOffer关闭，此次不执行", "SYSTEM");
                logger.warn("定时开关isCreateOverdueOffer关闭，此次不执行");
            }
        } catch (Exception e) {
            logger.error("创建逾期用户的报盘文件异常！" + e.getMessage());
            int length = e.getMessage().length();
            loanLogService.createLog("offerCreate", "error","生成今日逾期报盘出错........"+ (length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage()),"system");
        }
    }

    /**
     * 创建还款日用户的报盘文件
     */
    public void createRepaymentDateOffer() {
        try {
            logger.info("创建正常还款报盘开始===============");
            loanLogService.createLog("CreateOfferJob", "info","创建正常还款报盘开始........", "SYSTEM");
            String isAutoDeduct = sysParamDefineService.getSysParamValue("sysJob", "isCreateRepaymentDateOffer");
            if (!Const.isClosing.equals(isAutoDeduct)) {
                if (offerConfigService.isCanCreateNormalOffer()) {
                    int threadCount = 10;
                    multithreadingCreateOfferByType(threadCount,IOfferInfoService.ZHENGCHANG);
                } else {
                    loanLogService.createLog("CreateOfferJob", "info","createRepaymentDateOffer不在执行时间范围内，此次不执行","SYSTEM");
                    logger.warn("createRepaymentDateOffer不在执行时间范围内，此次不执行");
                }
            } else {
                loanLogService.createLog("CreateOfferJob", "info","定时开关isCreateRepaymentDateOffer关闭，此次不执行", "SYSTEM");
                logger.warn("定时开关isCreateRepaymentDateOffer关闭，此次不执行");
            }
        } catch (Exception e) {
            logger.error("创建还款日用户的报盘文件异常！" + e.getMessage());
            int length = e.getMessage().length();
            loanLogService.createLog("offerCreate", "error","生成今日还款日报盘出错........"+ (length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage()),"system");
        }
    }

    /**
     * 创建特殊还款用户的报盘文件
     */
    public void createSpecialOffer() {
        try {
            logger.info("创建特殊还款报盘开始===============");
            loanLogService.createLog("CreateOfferJob", "info","创建特殊还款报盘开始........", "SYSTEM");
            String isAutoDeduct = sysParamDefineService.getSysParamValue("sysJob", "isCreateSpecialOffer");
            if (!Const.isClosing.equals(isAutoDeduct)) {
                if (CollectionUtils.isNotEmpty(offerConfigService.queryAllDayDebitTypeList())) {
                    int threadCount = 5;
                    multithreadingCreateOfferByType(threadCount,IOfferInfoService.TIQIANKOUKUAN);
//                  multithreadingCreateOfferByType(threadCount,IOfferInfoService.TIQIANJIEQING); 关闭提前结清的自动报盘
                } else {
                    loanLogService.createLog("CreateOfferJob", "info","createSpecialOffer不在执行时间范围内，此次不执行", "SYSTEM");
                    logger.warn("createSpecialOffer不在执行时间范围内，此次不执行");
                }
            } else {
                loanLogService.createLog("CreateOfferJob", "info","定时开关isCreateSpecialOffer关闭，此次不执行", "SYSTEM");
                logger.warn("定时开关isCreateSpecialOffer关闭，此次不执行");
            }
        } catch (Exception e) {
            logger.error("创建特殊还款用户的报盘文件异常！" + e.getMessage());
            int length = e.getMessage().length();
            loanLogService.createLog("offerCreate", "error","生成今日特殊还款报盘出错........"+ (length > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage()),"system");

        }
    }

    /**
     * 多线程创建报盘文件
     * @param threadCount 线程数
     * @param offerType 报盘类型
     * @throws InterruptedException
     */
    private void multithreadingCreateOfferByType(int threadCount, final int offerType) {
        boolean allDone = false;
        ExecutorService service = null;
        try {
            // 设置有几个线程来处理下面的工作
            service = Executors.newFixedThreadPool(threadCount);
            // 获得所有的营业部级别的网点
            ComOrganization comOrganization = new ComOrganization();
            comOrganization.setvLevel(ComOrganizationEnum.get(ComOrganizationEnum.INDEX_CITY).getName());
            List<ComOrganization> salesDepList = comOrganizationService.findListByVo(comOrganization);
            // 循环营业部，已营业部为单位，多线程处理报盘生成
            for (final ComOrganization sd : salesDepList) {
                service.execute(new Runnable() {
                    public void run() {
                        int indexCount = offerCreateService.createOfferRecordWithOrgAndType(sd, offerType);
                        loanLogService.logForOfferCreate(sd.getName(),indexCount, offerType);
                    }
                });
            }
            allDone = service.awaitTermination(10, TimeUnit.MINUTES);// 线程启动15分钟，15分钟后关闭
        } catch (Exception e) {
            logger.error("multithreadingCreateOfferByType多线程创建报盘文件异常！" + e.getMessage(), e);
            throw new RuntimeException("多线程创建报盘文件异常!", e);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
        logger.info("all thread complete timeout = " + !allDone);
    }
}
