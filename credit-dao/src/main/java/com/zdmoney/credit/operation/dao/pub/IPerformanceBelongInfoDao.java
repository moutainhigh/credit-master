package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.PerformanceBelongInfo;

public interface IPerformanceBelongInfoDao extends IBaseDao<PerformanceBelongInfo> {
    
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
}
