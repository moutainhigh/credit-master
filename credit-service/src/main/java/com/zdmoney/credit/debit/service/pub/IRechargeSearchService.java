package com.zdmoney.credit.debit.service.pub;

public interface IRechargeSearchService {

    /**
     * 查询和处理外贸3线上还款和线下实收扣款结果
     */
    public void getSearchResult();

    /**
     * 查询和处理外贸3线上还款扣款结果
     */
    public void searchOnlineDebitResult();

    /**
     * 查询和处理外贸3线下实收扣款结果
     */
    public void searchOfflineDebitResult();
}
