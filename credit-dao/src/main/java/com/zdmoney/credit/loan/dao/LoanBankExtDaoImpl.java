package com.zdmoney.credit.loan.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBankExtDao;
import com.zdmoney.credit.loan.domain.LoanBankExt;

@Repository
public class LoanBankExtDaoImpl extends BaseDaoImpl<LoanBankExt> implements ILoanBankExtDao {
    
    public Pager queryOffLineLoanInfo(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryOffLineLoanInfoDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryOffLineLoanInfoCount");
        return doPager(pager, params);
    }
}
