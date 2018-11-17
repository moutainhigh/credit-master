package com.zdmoney.credit.job;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.json.JackJsonUtil;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.trustOffer.service.pub.ITrustOfferFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ym10094 on 2017/4/5.
 */
@Service
public class RefundedOfMoneyConfirmationJob {
    private Logger logger = LoggerFactory.getLogger(RefundedOfMoneyConfirmationJob.class);
    @Autowired
    private ITrustOfferFlowService trustOfferFlowService;

    @Autowired
    private ILoanLogService loanLogService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private IWorkDayInfoService workDayInfoService;

    public void excuteUploadRefundedOfMoneyConfirmationBookFile(){
        logger.info("开始执行回款确认书文件上传job");
        Date tradeDate = Dates.getBeforeDays(1);
        this.excuteUploadRefundedOfMoneyConfirmationBookFile(tradeDate);
    }

    public void excuteUploadRefundedOfMoneyConfirmationBookFile(Date tradeDate){
        loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", "开始上传回款确认书文件到渤海===============", "SYSTEM");
        Date currDate = Dates.getCurrDate();
        Date tempTradeDate = tradeDate;
        if(!workDayInfoService.isWorkDay(currDate)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "节假日时间不允许做上传！");
        }
        List<String> fundsSourcesList = new  ArrayList();
        fundsSourcesList.add(FundsSourcesTypeEnum.渤海信托.getValue());
        fundsSourcesList.add(FundsSourcesTypeEnum.渤海2.getValue());
        fundsSourcesList.add(FundsSourcesTypeEnum.华瑞渤海.getValue());
        JschSftpUtils jschSftpUtils = null;
        try {
            jschSftpUtils = connectBhxtFtpService.getFtpBhxtConnectJsch();
            if (!jschSftpUtils.login()) {
                logger.info("登录渤海ftp服务器失败!");
                return ;
            }
            for (String fundSources : fundsSourcesList) {
                try {
                    if ((fundSources.equals(FundsSourcesTypeEnum.渤海2.getValue()) || fundSources.equals(FundsSourcesTypeEnum.渤海信托.getValue())) && workDayInfoService.isFirstWorkDay(currDate)) {
                        tradeDate = workDayInfoService.getPreviousWorkDayInfoByParams(currDate);
                    }
                    //华瑞渤海第一个工作日
                    if (fundSources.equals(FundsSourcesTypeEnum.华瑞渤海.getValue()) && workDayInfoService.isFirstWorkDay(currDate)) {
                        trustOfferFlowService.firstWorkDayUploadRefundedOfMoneyConfirmationFileToBh(fundSources, tempTradeDate, jschSftpUtils);
                        continue;
                    }
                    if (trustOfferFlowService.fundsSourceIsUpload(fundSources, tradeDate)) {
                        continue;
                    }
                    if (trustOfferFlowService.checkOutUploadFileData(fundSources, tradeDate)) {
                        logger.info("数据校验有误不上传！");
                        continue;
                    }
                    boolean status = trustOfferFlowService.uploadRefundedOfMoneyConfirmationFileToBh(fundSources, tradeDate, jschSftpUtils);
                    trustOfferFlowService.insertUploadHKQRSLog(status, fundSources, tradeDate);
                    if (status) {
                        loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("上传{0}回款确认书文件到渤海成功！===============", fundSources), "SYSTEM");
                        logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--上传{}回款确认书文件到渤海成功！===============", fundSources);
                        continue;
                    }
                    loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("上传{0}回款确认书文件到渤海失败！===============", fundSources), "SYSTEM");
                    logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--上传{}回款确认书文件到渤海失败！===============", fundSources);
                }catch (Exception e){
                    e.printStackTrace();
                    loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("{0}上传回款确认书文件到渤海异常：{1}===============", fundSources,e.getMessage()), "SYSTEM");
                    logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--{}上传回款确认书文件到渤海异常：{}===============", fundSources,e.getMessage());
                }
            }
            //重新上传失败的
            trustOfferFlowService.againUploadFailIsHHQRSToBh(jschSftpUtils);
        }catch (Exception e){
            e.printStackTrace();
            loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("上传回款确认书文件到渤海异常：{0}===============",e.getMessage()), "SYSTEM");
            logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--上传回款确认书文件到渤海异常：{}===============",e.getMessage());
        }finally {
            if (jschSftpUtils != null) {
                jschSftpUtils.logout();
            }
            logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--回款确认书文件上传Job结束！");
            loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", "上传回款确认书文件到渤海结束===============", "SYSTEM");
        }
    }
}
