package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.operation.dao.pub.ILoanContractInfoDao;
import com.zdmoney.credit.operation.domain.LoanContractInfo;

@Repository
public class LoanContractInfoDaoImpl extends BaseDaoImpl<LoanContractInfo> implements ILoanContractInfoDao {

    public List<Map<String, Object>> getCrmOptionInfo(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getCrmOptionInfo",params);
    }
}
