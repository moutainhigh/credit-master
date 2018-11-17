package com.zdmoney.credit.repay.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.AbsOneTimeSettlementAdvice;

import java.util.List;

/**
 * Created by ym10094 on 2016/12/2.
 */
public interface IAbsOneTimeSettlementAdvicedDao extends IBaseDao<AbsOneTimeSettlementAdvice>{
    /**
     * 批量插入
     * @param list
     */
    public void insertBatch(List<AbsOneTimeSettlementAdvice> list);

    /**
     * 根据状态查询 一次结清通知记录
     * @param adviceStates
     * @return
     */
    public  List<AbsOneTimeSettlementAdvice> queryAbsOneTimeSettlementAdviceByStates(String[] adviceStates);
}
