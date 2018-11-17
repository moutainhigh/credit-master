package com.zdmoney.credit.loan.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanLogDao;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;


@Service
@Transactional
public class LoanLogServiceImpl implements ILoanLogService{
    private static final Logger logger = Logger.getLogger(LoanLogServiceImpl.class);
    @Autowired
    private ILoanLogDao loanLogDao;
    
    @Autowired @Qualifier("sequencesServiceImpl")
    ISequencesService sequencesServiceImpl;
    
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void logForOfferCreate(String name, int indexCount, int offerType) {
        try {
            LoanLog loanLog = new LoanLog();
            loanLog.setId(sequencesServiceImpl
                    .getSequences(SequencesEnum.LOAN_LOG));
            loanLog.setCreateTime(new Date());
            loanLog.setCreator("SYSTEM");
            loanLog.setLogType("info");
            loanLog.setLogName("offerCreate");
            String type = null;
            switch (offerType) {
            case IOfferInfoService.ZHENGCHANG:
                type = "正常还款";
                break;
            case IOfferInfoService.YUQI:
                type = "逾期";
                break;
            case IOfferInfoService.TIQIANKOUKUAN:
                type = "提前还款";
                break;
            case IOfferInfoService.TIQIANJIEQING:
                type = "提前结清";
                break;
            default:
                type = "未知";
            }
            loanLog.setContent("定时创建报盘文件，类别：" + type + "，营业部：" + name
                    + ", 生成报盘文件数：" + indexCount);
            loanLogDao.insert(loanLog);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void createLog(String logName, String logType, String content,String creator) {
        try {
            LoanLog loanLog = new LoanLog();
            loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_LOG));
            loanLog.setCreateTime(new Date());
            loanLog.setCreator(creator);
            loanLog.setLogType(logType);
            loanLog.setLogName(logName);
            loanLog.setContent(content);
            loanLogDao.insert(loanLog);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void createLog(Long objectId,String content,String logName, String logType, String memo) {
        try {
            LoanLog loanLog = new LoanLog();
            loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_LOG));
            loanLog.setObjectId(objectId);
            loanLog.setContent(content);
            loanLog.setLogType(logType);
            loanLog.setLogName(logName);
            loanLog.setMemo(memo);
            loanLogDao.insert(loanLog);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    
    /**
     * 创建loan流程日志
     * @param objectId
     * @param logName
     * @param logType
     * @param loanFlowState
     * @param content
     * @param creator
     */
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void createLog(Long objectId, String logName, String logType, String loanFlowState,String content,String creator) {
        try {
            LoanLog loanLog = new LoanLog();
            loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_LOG));
            loanLog.setObjectId(objectId);
            loanLog.setCreator(creator);
            loanLog.setCreateTime(new Date());
            loanLog.setContent(content);
            loanLog.setLogType(logType);
            loanLog.setLogName(logName);
            loanLog.setMemo(loanFlowState);
            loanLogDao.insert(loanLog);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }


    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void createLog(LoanLog loanLog) {
        loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_LOG));
        try {
            loanLogDao.insert(loanLog);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    public Pager findWithPg(LoanLog loanLog) {
        return loanLogDao.findWithPg(loanLog);
    }
}
