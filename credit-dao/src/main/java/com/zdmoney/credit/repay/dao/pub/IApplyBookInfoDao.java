package com.zdmoney.credit.repay.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.ApplyBookInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by ym10094 on 2017/1/10.
 */
public interface IApplyBookInfoDao extends IBaseDao<ApplyBookInfo> {

    public int queryCurrentDayCount(String fundsSources);

    public List<ApplyBookInfo> queryDayApplyBookInfos(String fundsSource ,Date queryDay);

    public ApplyBookInfo queryApplyBookInfoBybatchNum(String batchNum);
}
