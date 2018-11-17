package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;

public interface ILoanManageSalesDepLogService {
    
    /**
     * 
     * 更新最新一条日志信息
     * @param loanManageSalesDepLog
     * @return
     */
    public int updateLastManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog);
    
    /**
     * 
     * 记录日志信息
     * @param loanManageSalesDepLog
     * @return
     */
    public LoanManageSalesDepLog insertLoanManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog);
    
    /**
     * 通过loanId查询
     * @param loanId
     * @return
     */
    public LoanManageSalesDepLog findByLoanId(Long loanId);
}
