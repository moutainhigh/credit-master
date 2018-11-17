package com.zdmoney.credit.repay.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IApplyBookInfoDao;
import com.zdmoney.credit.repay.vo.ApplyBookInfo;
import org.olap4j.impl.ArrayMap;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/1/10.
 */
@Repository
public class ApplyBookInfoDaoImpl  extends BaseDaoImpl<ApplyBookInfo> implements IApplyBookInfoDao {
    @Override
    public int queryCurrentDayCount(String fundsSources) {
        Map<String,Object> param = new ArrayMap<>();
        param.put("fundsSources",fundsSources);
        param.put("createTime",new Date());
        return getSqlSession().selectOne(getIbatisMapperNameSpace()+".queryCurrentDayCount",param);
    }

    @Override
    public List<ApplyBookInfo> queryDayApplyBookInfos(String fundsSource, Date queryDay) {
        Map<String,Object> param = new ArrayMap<>();
        param.put("fundsSource",fundsSource);
        param.put("queryDate",queryDay);
        return getSqlSession().selectList(getIbatisMapperNameSpace()+".queryApplyBookInfos",param);
    }

    @Override
    public ApplyBookInfo queryApplyBookInfoBybatchNum(String batchNum) {
        Map<String,Object> param = new ArrayMap<>();
        param.put("batchNum",batchNum);
        List<ApplyBookInfo> applyBookInfoList = getSqlSession().selectList(getIbatisMapperNameSpace()+".queryApplyBookInfos",param);
        if (CollectionUtils.isEmpty(applyBookInfoList)) {
            return null;
        }
        return applyBookInfoList.get(0);
    }
}
