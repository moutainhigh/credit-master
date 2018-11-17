package com.zdmoney.credit.payment.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferBatch;

public interface IThirdLineOfferBatchDao extends IBaseDao<ThirdLineOfferBatch>{
    public List<ThirdLineOfferBatch> findHaTwoOfferBatch();
    
    public List<ThirdLineOfferBatch> findHaTwoOfferBatchNotExport();
    
    /**
     * 查询未导出报盘文件，同一债权最新的批次
     * @return
     */
    public List<ThirdLineOfferBatch> findThirdLineOfferBatch();
    
    public ThirdLineOfferBatch findOfferBatchId();
    
    /**
     * 查询最近未导出报盘文件的一个批次
     * @return
     */
    public ThirdLineOfferBatch findLatelyOfferBatchInfo();
}
