package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanLog;

public interface ILoanLogService {

    /**
     * 创建报盘的日志记录
     * @param name 营业部名称
     * @param indexCount 创建个数
     * @param offerType 报盘文件类型
     */
    public void logForOfferCreate(String name, int indexCount, int offerType);

    /**
     * 创建log
     * @param logName
     * @param logType
     * @param content
     * @param creator
     */
    public void createLog(String logName, String logType, String content,String creator);
    
    /**
     * 记录log日志
     * @param objectId
     * @param content
     * @param logName
     * @param logType
     * @param memo
     */
    public void createLog(Long objectId,String content,String logName, String logType, String memo);
    
    /**
     * 创建loan流程日志
     * @param objectId
     * @param logName
     * @param logType
     * @param loanFlowState
     * @param content
     * @param creator
     */
    public void createLog(Long objectId, String logName, String logType, String loanFlowState,String content,String creator);
    
    /**
     * 记录log日志
     * @param loanLog
     */
    public void createLog(LoanLog loanLog);
    
    /**
     * 带分页查询
     * @param loanLog 条件实体对象
     * @return
     */
    public Pager findWithPg(LoanLog loanLog);
}
