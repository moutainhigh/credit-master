package com.zdmoney.credit.offer.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.domain.OfferInfo;

public interface IOffLineOfferService {

    /**
     * 分页查询线下还款报盘信息
     * @param params
     * @return
     */
    public Pager searchOfferInfoWithPgByMap(Map<String, Object> params);

    /**
     * 查询线下还款报盘信息
     * @param params
     * @return
     */
    public List<OfferInfo> findOffLineOfferInfoList(Map<String, Object> params);

    /**
     * 查询线下还款报盘信息（用于导出报盘文件）
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryOffLineOfferInfo(Map<String, Object> params);

    /**
     * 保存报盘流水导出记录
     * @param offerId
     */
    public void saveOfferExportRecord(Long transId);

    /**
     * 关闭线下还款报盘
     * @param offerId
     */
    public void closeOffer(Long offerId);

    /**
     * 分页查询线下还款回盘信息
     * @param params
     * @return
     */
    public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params);

    /**
     * 线下还款回盘导入
     * @param sheetDataList
     */
    public void importReturnOffer(List<Map<String, String>> sheetDataList);
    
    /**
     * 分页查询线下还款债权相关信息
     * @param params
     * @return
     */
    public Pager queryOffLineLoanInfo(Map<String, Object> params);

    /**
     * 设置银行卡行别、行号
     * @param bankIdArr
     * @param regionType
     */
    public void setRegionType(String[] bankIdArr, String regionType);

    /**
     * 判断线下还款报盘文件是否已导出
     * @param offerId
     * @return
     */
    public boolean isExportOffer(Long offerId);
}
