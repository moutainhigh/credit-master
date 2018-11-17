package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.operation.dao.pub.IPerformanceBelongInfoDao;
import com.zdmoney.credit.operation.domain.PerformanceBelongInfo;

@Repository
public class PerformanceBelongInfoDaoImpl extends BaseDaoImpl<PerformanceBelongInfo> implements IPerformanceBelongInfoDao {

    public List<Map<String, Object>> getSalesManOptionInfo(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSalesManOptionInfo",params);
    }

    public List<Map<String, Object>> getSalesTeamOptionInfo(
            Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSalesTeamOptionInfo",params);
    }
}
