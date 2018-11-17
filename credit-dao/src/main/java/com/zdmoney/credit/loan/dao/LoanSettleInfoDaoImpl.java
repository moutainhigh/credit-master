package com.zdmoney.credit.loan.dao;


import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanSettleInfoDao;
import com.zdmoney.credit.loan.domain.LoanSettleInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YM10112 2017年10月17日 下午4:36:57
 */
@Repository
public class LoanSettleInfoDaoImpl extends BaseDaoImpl<LoanSettleInfo> implements ILoanSettleInfoDao {

    @Override
    public LoanSettleInfo findLoanSettlenfoByLoanId(Long loanId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", loanId);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanSettlenfoByLoanId", params);
    }

}
