package com.zdmoney.credit.debit.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IDebitTransactionDao  extends IBaseDao<DebitTransaction>{
    
    /**
     * @param serialNo
     * @return
     */
    public DebitTransaction findBySerialNo(String serialNo);

    public List<DebitTransaction> findByStateAndLoan(OfferTransactionStateEnum state,Long loanId);

    public DebitTransaction queryDebitTransactionByConditions(Map<String, Object> params);
    
    /**
     * 查询外贸3已发送报盘的批次号
     * @param params
     * @return
     */
    public List<DebitTransaction> queryDebitBatchNos(Map<String, Object> params);
}
