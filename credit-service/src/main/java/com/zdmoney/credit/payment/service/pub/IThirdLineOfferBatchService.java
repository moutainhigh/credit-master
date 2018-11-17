package com.zdmoney.credit.payment.service.pub;

import java.util.List;

import com.zdmoney.credit.payment.domain.ThirdLineOfferBatch;

/**
 * 第三方线下代付 报盘批次表接口类
 * @author zhangln
 */
public interface IThirdLineOfferBatchService {
    
    /**
     * 新增批次
     * @param haTwoOfferBatch
     */
    public void insert(ThirdLineOfferBatch haTwoOfferBatch);
    
    /**
     * 更新批次
     * @param haTwoOfferBatch
     */
    public void update(ThirdLineOfferBatch haTwoOfferBatch);

    /**
     * 查询批次表
     * @return
     */
    public List<ThirdLineOfferBatch> findHaTwoOfferBatch();

    /**
     * 查询批次表
     * @return
     */
    public List<ThirdLineOfferBatch> findThirdLineOfferBatch();

    /**
     * 查询最近未导出报盘文件的一个批次
     * @return
     */
    public ThirdLineOfferBatch findOfferBatchId();

    /**
     * 根据主键ID查询批次
     * @param id
     * @return
     */
    public ThirdLineOfferBatch getThirdLineOfferBatch(Long id);

    /**
     * 查询最近未导出报盘文件的一个批次
     * @return
     */
    public ThirdLineOfferBatch findLatelyOfferBatchInfo();
    
    /**
     * 根据指定条件查询批次信息
     * @param offerBatch
     * @return
     */
    public List<ThirdLineOfferBatch> findListByVo(ThirdLineOfferBatch offerBatch);
}
