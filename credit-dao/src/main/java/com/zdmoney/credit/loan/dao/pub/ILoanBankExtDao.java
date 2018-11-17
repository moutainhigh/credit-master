package com.zdmoney.credit.loan.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanBankExt;

public interface ILoanBankExtDao extends IBaseDao<LoanBankExt> {
    
    /**
     * 分页查询线下还款债权相关信息
     * @param params
     * @return
     */
    public Pager queryOffLineLoanInfo(Map<String, Object> params);
}
