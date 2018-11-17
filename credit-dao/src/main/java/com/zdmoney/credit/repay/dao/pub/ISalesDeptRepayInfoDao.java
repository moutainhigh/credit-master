package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.SalesDeptRepayInfo;

public interface ISalesDeptRepayInfoDao extends IBaseDao<SalesDeptRepayInfo>{
    
    /**
     * 
     * 查询当前用户所属机构下的所有营业网点信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getSalesDeptInfo(Map<String,Object> params);
}
