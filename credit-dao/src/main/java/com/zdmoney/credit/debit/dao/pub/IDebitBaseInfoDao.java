package com.zdmoney.credit.debit.dao.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IDebitBaseInfoDao extends IBaseDao<DebitBaseInfo> {

    public List<DebitBaseInfo> queryTodayNeedSendOfferDebitList(Map<String, Object> params);

    public Integer getOfferCountForYet3Deduct(DebitBaseInfo debitBaseInfo);

    /**
     * 根据报盘日期和多个状态得到报盘文件（只包含自动划扣）
     * 
     * @param loanId
     * @param currDate
     * @param states
     * @return
     */
    public List<DebitBaseInfo> findOfferByOfferDateAndStates(Long loanId,Date currDate, String[] states);

}
