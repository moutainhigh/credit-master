package com.zdmoney.credit.payment.service.pub;

import java.util.List;
import java.util.Map;

/**
 * 第三方线下代付接口类
 * @author zhangln
 */

public interface IThirdUnderLinePaymentService {
    /**
     * 生成报盘
     */
    public void createHaTwoOffer();
    /**
     * 导入回盘
     * @param dataList
     */
    public void updateHaTwoOffer(List<Map<String, String>> dataList);
    
    /**
     * 生成报盘相关信息（包括报盘信息、报盘流水信息、批次信息）
     */
    public void createOffer(Map<String, Object> map);
    
    /**
     * 创建当天批次号
     * @return
     */
    public String createBatchNumber();
    
    /**
     * 债权是否可以退回
     * @param loanId
     * @return
     */
    public boolean isLoanBack(Long loanId);
}
