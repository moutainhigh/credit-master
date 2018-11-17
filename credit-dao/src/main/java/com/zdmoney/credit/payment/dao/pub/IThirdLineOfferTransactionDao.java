package com.zdmoney.credit.payment.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferTransaction;

public interface IThirdLineOfferTransactionDao extends IBaseDao<ThirdLineOfferTransaction>{
    /**
     * 根据报盘批次Id查询所有的报盘状态表信息
     * @param batchId
     * @return
     */
    public List<ThirdLineOfferTransaction> findHaTwoOfferTransactionByBatchId(Long batchId);
    /**
     * 根据流水号查询报盘状态表信息
     * @param paramMap
     * @return
     */
    public ThirdLineOfferTransaction findHaTwoOfferTransactionByMap(Map<String, Object> paramMap);
    
    /**
     * 根据批次号相关条件查询报盘流水表信息
     * @param params
     * @return
     */
    public List<ThirdLineOfferTransaction> findOfferTransactionListByMap(Map<String, Object> params);
}
