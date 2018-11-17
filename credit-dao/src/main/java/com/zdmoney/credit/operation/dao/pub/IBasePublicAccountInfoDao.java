package com.zdmoney.credit.operation.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;

public interface IBasePublicAccountInfoDao extends IBaseDao<BasePublicAccountInfo> {
    
    /**
     * 查询对公账户已认领结果
     * @param basePublicAccountInfo
     * @return
     */
    public List<Map<String,Object>> findPublicAccountReceiveInfo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 查询对公账户还款认领情况表
     * @param basePublicAccountInfo
     * @return
     */
    public List<Map<String,Object>> findPublicAccountRepayReceiveInfo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 查询对公账户信息
     * @param basePublicAccountInfo
     * @return
     */
    public List<BasePublicAccountInfo> findQueryResultList(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 撤销认领
     * @param basePublicAccountInfo
     * @return
     */
    public int updateAccountInfoForCancel(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 修改状态为已导出
     * @param params
     * @return
     */
    public int updateAccountInfoForExport(Map<String,Object> params);

    /**
     * 查询结果
     * @param basePublicAccountInfo
     * @return
     */
    List<Map<String,Object>> findQueryResultMapList(BasePublicAccountInfo basePublicAccountInfo);
}
