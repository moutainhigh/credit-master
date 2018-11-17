package com.zdmoney.credit.payment.service.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.payment.domain.OfferExportBatch;
import com.zdmoney.credit.payment.domain.OfferExportInfo;
import com.zdmoney.credit.payment.domain.ThirdLineOfferTransaction;

public interface IThirdLineOfferTransactionService {

    /**
     * 根据批次Id查询报盘流水信息
     * @param map
     * @return
     */
    public List<ThirdLineOfferTransaction> findListByMap(Map<String, Object> map);

    /**
     * 更新报盘流水信息
     * @param map
     * @return
     */
    public void updateState(Long batchId);

    /**
     * 更加日常查找当前时间通联线下代付的批次信息
     * @param offerExportBatch
     * @return
     */
    public OfferExportBatch findOfferExportBatch(OfferExportBatch offerExportBatch);

    /**
     * 新增一条批次信息到OFFER_EXPORT_BATCH
     * @param offerExportBatch
     * @return
     */
    public int insertOfferExportBatch(OfferExportBatch offerExportBatch);

    /**
     * 更新一条批次信息到OFFER_EXPORT_BATCH
     * @param offerExportBatch
     * @return
     */
    public int updateOfferExportBatch(OfferExportBatch offerExportBatch);

    /**
     * 获取国民信托查询信息
     * @return
     */
    public List<OfferExportInfo> getGuoMinXinTuoThirdLine();

    /**
     * 获取国民信托查询信息财务放款的总金额
     * @return
     */
    public BigDecimal getGuoMinXinTuoThirdLineTotalAmount();
    
    /**
     * 根据批次号相关条件查询报盘流水表信息
     * @param params
     * @return
     */
    public List<ThirdLineOfferTransaction> findOfferTransactionListByMap(Map<String, Object> params);
}
