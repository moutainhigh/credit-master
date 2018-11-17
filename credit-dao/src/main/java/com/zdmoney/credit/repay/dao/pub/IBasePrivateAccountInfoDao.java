package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.BasePrivateAccountInfo;

public interface IBasePrivateAccountInfoDao extends IBaseDao<BasePrivateAccountInfo> {
    
    /**
     * 查询对私还款已认领结果
     * @param basePrivateAccountInfo
     * @return
     */
    public List<Map<String,Object>> findPrivateAccountReceiveInfo(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 对私还款修改状态为已导出
     * @param params
     * @return
     */
    public int updateAccountInfoForExport(Map<String,Object> params);

    /**
     * 对私还款撤销领取或渠道确认
     * @param basePrivateAccountInfo
     * @return
     */
    public int updateAccountInfoForCancel(BasePrivateAccountInfo basePrivateAccountInfo);

    /**
     * 查询结果
     * @param basePrivateAccountInfo
     * @return
     */
    public List<Map<String,Object>> findQueryResultMapList(BasePrivateAccountInfo basePrivateAccountInfo);
}
