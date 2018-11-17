package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.LoanContractInfo;

public interface ILoanContractInfoDao extends IBaseDao<LoanContractInfo> {
    
    /**
     * 查询当前机构下的客服人员信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getCrmOptionInfo(Map<String,Object> params);
}
