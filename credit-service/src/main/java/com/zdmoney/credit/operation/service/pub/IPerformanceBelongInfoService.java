package com.zdmoney.credit.operation.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.operation.domain.PerformanceBelongInfo;

public interface IPerformanceBelongInfoService {
    
    /**
     * 带分页查询
     * @param performanceBelongInfo 条件实体对象
     * @return
     */
    public Pager findWithPg(PerformanceBelongInfo performanceBelongInfo);
    
    /**
     * 查询当前机构下的销售人员信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getSalesManOptionInfo(Map<String,Object> params);
    
    /**
     * 查询当前机构下的销售团队信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getSalesTeamOptionInfo(Map<String,Object> params);
    
    /**
     * 变更业绩归属
     * @param loanBase
     * @return
     */
    public int updatePerformanceBelongInfo(LoanBase loanBase);
}
