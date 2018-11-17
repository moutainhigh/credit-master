package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanSettleInfo;

/**
 * @author YM10112 2017年10月17日 下午4:37:05
 */
public interface ILoanSettleInfoDao extends IBaseDao<LoanSettleInfo> {
    /**
     * 根据债权Id得到数据
     *
     * @param loanId
     * @return
     */
    public LoanSettleInfo findLoanSettlenfoByLoanId(Long loanId);
}
