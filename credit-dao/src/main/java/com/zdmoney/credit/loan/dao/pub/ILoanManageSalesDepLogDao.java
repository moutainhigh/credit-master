package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;

/**
 * 
 * @author 00232949
 *
 */
public interface ILoanManageSalesDepLogDao  extends IBaseDao<LoanManageSalesDepLog>{
    
    /**
     * 更新最近一条日志
     * @param loanManageSalesDepLog
     * @return
     */
    public int updateLastManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog);
    
    /**
     * 根据loanId查询
     * @param loanId
     * @return
     */
    public LoanManageSalesDepLog findByLoanId(Long loanId);
}
