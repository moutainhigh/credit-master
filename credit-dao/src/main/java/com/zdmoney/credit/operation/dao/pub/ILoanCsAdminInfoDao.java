package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.LoanCsAdminInfo;

public interface ILoanCsAdminInfoDao extends IBaseDao<LoanCsAdminInfo> {
    
    /**
     * 查询当前机构下的客服人员信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getCrmOptionInfo(Map<String,Object> params);
    /**
     * 查询所有催收管理室员工
     * @param collectors
     * @return
     */
    public Pager getCollectors(Map<String,Object> params);
}
