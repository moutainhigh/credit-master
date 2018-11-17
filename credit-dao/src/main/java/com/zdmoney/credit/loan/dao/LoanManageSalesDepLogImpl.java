package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanManageSalesDepLogDao;
import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;

@Repository
public class LoanManageSalesDepLogImpl extends BaseDaoImpl<LoanManageSalesDepLog> implements ILoanManageSalesDepLogDao {

    public int updateLastManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog) {
        return getSqlSession().update(getIbatisMapperNameSpace() + ".updateLastManageSalesDeptLog",loanManageSalesDepLog);
    }

    /**
     * 根据loanId查询
     * @param loanId
     * @return
     */
    public LoanManageSalesDepLog findByLoanId(Long loanId) {
        LoanManageSalesDepLog log = new LoanManageSalesDepLog();
        log.setLoanId(loanId);
        List<LoanManageSalesDepLog> list = findListByVo(log);
        if(list!= null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
